package unity.server.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.jena.ontology.OntClass;
import org.gitlab4j.api.models.RepositoryFile;
import org.glassfish.jersey.server.mvc.Viewable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import unity.api.DXCommit;
import unity.api.DXException;
import unity.api.DXFile;
import unity.api.DXPackageReference;
import unity.api.DXProject;
import unity.api.DXTaskExecution;
import unity.api.DXTaskExecutionStatus;
import unity.ontology.UnityDXOntology;
import unity.repository.UnityDXBranch;
import unity.server.Controller;

public class ProjectController extends Controller
{
    private final DXCommit  commit;
    private final DXProject project;
    private final String    ref;

    public ProjectController(Controller parent, DXProject project) throws DXException
    {
        super(parent);
        this.project = project;
        commit = project.getRepository().getDefaultBranch();
        ref = project.getDefaultBranchName();
    }

    public ProjectController(Controller parent, DXProject project, DXCommit commit, String ref)
    {
        super(parent);
        this.project = project;
        this.commit = commit;
        this.ref = ref;
    }

    @POST
    @Path("console")
    public String doConsole() throws DXException, IOException
    {
        final String script = IOUtils.toString(getRequest().getInputStream(), StandardCharsets.UTF_8);
        final DXPackageReference packageReference = commit.getAsPackageReference();
        final DXTaskExecution execution = getSystem().submitTask("console", script, packageReference);

        for (int i = 0; i < 100; i++) {
            final DXTaskExecutionStatus status = getSystem().getTaskExecutionStatus(execution.getId());
            if (status == DXTaskExecutionStatus.ACTIVE || status == DXTaskExecutionStatus.QUEUED) {
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                }
                continue;
            }
            break;
        }

        return getSystem().getTaskExecutionResult(execution.getId());
    }

    @GET
    @Path("file")
    public String doGetFile(@QueryParam("path") String path) throws DXException, IOException
    {
        if (path == null || path.trim().isEmpty())
            return "";
        if (path.startsWith(project.getPathWithNamespace()))
            return IOUtils.toString(commit.getResourceAsStream(path.substring(project.getPathWithNamespace().length())),
                    StandardCharsets.UTF_8);
        while (path.startsWith("@"))
            path = path.substring(1);
        final String workspacePath = path.substring(0, path.indexOf("@"));
        path = path.substring(workspacePath.length() + 1);
        final String reference = path.substring(0, path.indexOf("@"));
        path = path.substring(reference.length() + 1);
        final DXProject project = getSystem().getProject(workspacePath);
        return IOUtils.toString(project.getRepository().getCommit(reference).getResourceAsStream(path),
                StandardCharsets.UTF_8);
    }

    @GET
    @Path("new")
    public Viewable doGetNewForm(@QueryParam("type") String type) throws DXException
    {
        try (InputStream input = commit.getResourceAsStream(((UnityDXBranch) commit).getOntology().getSchema(type))) {
            final String schema = IOUtils.toString(input, StandardCharsets.UTF_8);
            getRequest().setAttribute("schema", StringEscapeUtils.escapeEcmaScript(schema));
            return new Viewable("new", this);
        } catch (final IOException e) {
            throw new DXException(e);
        }
    }

    @POST
    @Path("new")
    public void doPostNew(@FormParam("name") String name, @FormParam("location") String location,
            @FormParam("type") String type, @FormParam("content") String content) throws DXException
    {
        try {
            final RepositoryFile file = new RepositoryFile();
            file.setContent(content);
            file.setFilePath(location.substring(project.getPathWithNamespace().length()) + "/" + name);
            getBranch().api().getRepositoryFileApi().createFile(file, project.getId(), getBranch().getName(), "New");

            final RepositoryFile metafile = new RepositoryFile();
            final JsonObject metadata = new JsonObject();

            final JsonObject context = new JsonObject();
            context.addProperty("@vocab", "http://dgms.io/ontologies/example#");
            metadata.add("@context", context);
            metadata.addProperty("@id", "");
            metadata.addProperty("@type", type);
            metadata.addProperty("name", name);
            metafile.setContent(metadata.toString());
            metafile.setFilePath(
                    location.substring(project.getPathWithNamespace().length()) + "/" + name + ".metadata.jsonld");
            getBranch().api().getRepositoryFileApi().createFile(metafile, project.getId(), getBranch().getName(),
                    "New");

            final URI uri = URI.create(getRequest().getContextPath() + "/" + getPath() + "/tree");
            getResponse().sendRedirect(uri.toString());
        } catch (final Exception e) {
            throw new DXException(e);
        }
    }

    @SuppressWarnings("deprecation")
    @POST
    @Path("run")
    public String doPostRun(@QueryParam("path") String path) throws DXException, IOException
    {
        final String input = IOUtils.toString(getRequest().getInputStream());
        final StringBuilder sb = new StringBuilder();
        sb.append("jsoniq version \"1.0\";");
        sb.append("import module namespace dgal = \"http://dgms.io/unity/modules/analytics/core\";");
        final JsonObject in = new Gson().fromJson(input, JsonObject.class);
        String model = in.get("model").getAsString().trim();

        model = model.substring(2, model.indexOf("}"));
        sb.append("import module namespace ns = \"" + model + "\";");

        sb.append("dgal:argmin(" + in.get("model").getAsString() + ", " + in.get("input").getAsJsonObject().toString()
                + ",\"" + in.get("objective").getAsString() + "\", " + in.get("options").getAsJsonObject().toString()
                + ")");

        final String expression = sb.toString();
        System.out.println(expression);
        final DXPackageReference packageReference = commit.getAsPackageReference();
        final DXTaskExecution execution = getSystem().submitTask(path, expression, packageReference);

        DXTaskExecutionStatus status = null;
        while (true) {
            status = getSystem().getTaskExecutionStatus(execution.getId());
            if (!(status == DXTaskExecutionStatus.ACTIVE || status == DXTaskExecutionStatus.QUEUED))
                break;
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
            }
        }
        switch (status) {
        case FAILED:
            return getSystem().getTaskExecutionError(execution.getId());
        case FINISHED:
            return getSystem().getTaskExecutionResult(execution.getId());
        case INTERRUPTED:
            return "INTERRUPTED";
        case KILLED:
            return "KILLED";
        default:
            throw new DXException("invalid task execution status returned: " + status);
        }
    }

    @POST
    @Path("save")
    public void doPostSave(@QueryParam("path") String path, String content) throws DXException
    {
        /*
         * if (path == null || path.trim().isEmpty() || commit instanceof DXBranch)
         * return; if (path.startsWith(project.getPathWithNamespace()))
         * //((DXBranch)commit). return
         * IOUtils.toString(commit.getResourceAsStream(path.substring(project.
         * getPathWithNamespace().length())), StandardCharsets.UTF_8); while
         * (path.startsWith("@")) path = path.substring(1); final String workspacePath =
         * path.substring(0, path.indexOf("@")); path =
         * path.substring(workspacePath.length() + 1); final String reference =
         * path.substring(0, path.indexOf("@")); path =
         * path.substring(reference.length() + 1); final DXProject project =
         * getSystem().getProject(workspacePath); return
         * IOUtils.toString(project.getRepository().getCommit(reference).
         * getResourceAsStream(path), StandardCharsets.UTF_8);
         *
         */
        System.out.println(path + "--" + content);
        /*
         *
         * try { final RepositoryFile file = new RepositoryFile();
         * file.setContent(content);
         * file.setFilePath(location.substring(project.getPathWithNamespace().length())
         * + "/" + name); getBranch().api().getRepositoryFileApi().createFile(file,
         * project.getId(), getBranch().getName(), "New"); final URI uri =
         * URI.create(getRequest().getContextPath() + "/" + getPath() + "/tree");
         * getResponse().sendRedirect(uri.toString()); } catch (final Exception e) {
         * throw new DXException(e); }
         */
    }

    public UnityDXBranch getBranch()
    {
        if (commit instanceof UnityDXBranch)
            return (UnityDXBranch) commit;
        return null;
    }

    public DXCommit getCommit()
    {
        return commit;
    }

    @GET
    @Path("console")
    public Viewable getConsole(@QueryParam("action") final String action)
    {
        if (action != null)
            return new Viewable("actions/" + action, this);
        return new Viewable("console", this);
    }

    public DXFile getFile(String path) throws DXException
    {
        if (path.startsWith(commit.getProject().getPathWithNamespace())) {
            path = path.substring(commit.getProject().getPathWithNamespace().length());
            return commit.getFile(path);
        }
        if (path.startsWith("@")) {
            final String[] parts = path.split("@");
            final String project = parts[1];
            final String version = parts[2];
            path = parts.length > 3 ? parts[3] : null;
            return getSession().getSystem().getProject(project).getRepository().getCommit(version).getFile(path);
        }
        throw new DXException("file not found");
    }

    @GET
    public Viewable getIndex()
    {
        return new Viewable("index", this);
    }

    @GET
    @Path("kernel")
    public Viewable getKernel(@QueryParam("action") final String action)
    {
        return new Viewable("kernel", this);
    }

    @GET
    @Path("ontology.json")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, Object>> getOntologyTree(@QueryParam("path") final String path) throws DXException
    {
        final List<Map<String, Object>> artifacts = new LinkedList<>();
        final UnityDXBranch branch = getBranch();
        if (branch == null)
            return artifacts;
        final UnityDXOntology ontology = branch.getOntology();
        List<OntClass> types;
        if ("#".equals(path))
            types = ontology.getArtifactTypes("http://dgms.io/ontologies/example#Artifact");
        else
            types = ontology.getArtifactTypes(path);
        for (final OntClass type : types) {
            final Map<String, Object> artifact = new HashMap<>();
            artifact.put("id", type.getURI());
            artifact.put("text", type.getLocalName());
            artifact.put("children", true);
            artifact.put("icon", "fa fa-cube");
            artifacts.add(artifact);
        }
        return artifacts;
    }

    @Override
    public String getPath()
    {
        return project.getPathWithNamespace();
    }

    public DXProject getProject()
    {
        return project;
    }

    public String getRef()
    {
        return ref;
    }

    @GET
    @Path("tree/{path: .*}")
    public Viewable getRepositoryResource(@QueryParam("action") final String action,
            @PathParam("path") List<PathSegment> paths)
    {
        String path;
        if (paths == null || paths.isEmpty())
            path = getProject().getPathWithNamespace();
        else
            path = paths.stream().map(p -> p.getPath()).collect(Collectors.joining("/"));
        final StringBuilder sb1 = new StringBuilder();
        sb1.append("[");
        final LinkedHashMap<String, String> pathComponents = new LinkedHashMap<>();
        getRequest().setAttribute("pathComponents", pathComponents);
        try {
            String[] pathSegments;
            if (!path.startsWith("@")) {
                pathSegments = path.split("/");
                final StringBuilder sb = new StringBuilder();
                for (final String pathSegment : pathSegments) {
                    sb.append("/" + pathSegment);
                    pathComponents.put(pathSegment, sb.toString());
                }

                int i = 0;
                final int len = pathSegments.length;
                for (final Entry<String, String> pathComponent : pathComponents.entrySet()) {
                    if (i == 0) {
                        i++;
                        continue;
                    }
                    sb1.append("\"" + pathComponent.getValue().substring(1) + "\"");
                    if (++i < len)
                        sb1.append(",");
                }
            } else {
                final int ind = path.lastIndexOf("@");

                if (path.length() == ind + 1)
                    pathSegments = new String[] { path };
                else {
                    pathSegments = new String[] { path.substring(0, ind + 1) };
                    pathSegments = concat(pathSegments, path.substring(ind + 1).split("/"));
                }

                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pathSegments.length; i++) {
                    if (i > 1)
                        sb.append("/" + pathSegments[i]);
                    else
                        sb.append(pathSegments[i]);
                    pathComponents.put(pathSegments[i], sb.toString());
                }

                int i = 0;
                final int len = pathSegments.length;
                for (final Entry<String, String> pathComponent : pathComponents.entrySet()) {

                    sb1.append("\"" + pathComponent.getValue() + "\"");
                    if (++i < len)
                        sb1.append(",");
                }
            }

        } catch (final Exception e) {
        }

        try {
            final DXFile file = getFile(path);
            getRequest().setAttribute("file", file);
            final String schema = file.getSchema();
            if (schema != null)
                getRequest().setAttribute("schema", StringEscapeUtils.escapeEcmaScript(schema));
            try (InputStream input = file.newInputStream()) {
                getRequest().setAttribute("content", IOUtils.toString(input, StandardCharsets.UTF_8));
            } catch (final Exception e) {
            }
        } catch (final Exception e) {
            try {
                final String id = path.substring(path.lastIndexOf("/"));
                getRequest().setAttribute("content", getSystem().getTaskExecutionResult(Long.parseLong(id)));
            } catch (final Exception ee) {
            }
        }

        sb1.append("]");

        if (path.startsWith("@")) {
            final String[] parts = path.split("@");
            final String project = parts[1];
            final String version = parts[2];
            getRequest().setAttribute("prefix", "@" + project + "@" + version + "@");
        }

        getRequest().setAttribute("navComponents", sb1.toString());

        if (action != null)
            return new Viewable("actions/" + action, this);
        return new Viewable("tree", this);
    }

    @GET
    @Path("tree")
    public Viewable getRepositoryResource1(@QueryParam("action") final String action,
            @PathParam("path") List<PathSegment> paths)
    {
        return getRepositoryResource(action, null);
    }

    @GET
    @Path("tree.json")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, Object>> getRepositoryTree(@Context UriInfo info, @QueryParam("path") String path)
            throws DXException
    {
        final String fullPath = path;
        final List<Map<String, Object>> artifacts = new LinkedList<>();
        if ("#".equals(path)) {
            Map<String, Object> artifact = new HashMap<>();
            artifact.put("id", project.getPathWithNamespace());
            artifact.put("text", project.getName());
            artifact.put("children", true);
            artifact.put("icon", "octicon octicon-repo");
            artifacts.add(artifact);

            for (final DXPackageReference packageReference : commit.getDependencies().collect(Collectors.toList())) {
                artifact = new HashMap<>();
                artifact.put("id", packageReference.getName() + "@" + packageReference.getVersion() + "@");
                artifact.put("text", packageReference.getName() + " [" + packageReference.getVersion() + "]");
                artifact.put("children", true);
                artifact.put("icon", "octicon octicon-plug");
                artifacts.add(artifact);
            }

        } else if (path.startsWith(project.getPathWithNamespace())) {
            for (final DXFile file : commit.getFiles(path.substring(project.getPathWithNamespace().length()), false)
                    .collect(Collectors.toList())) {
                // if (!file.isDirectory())
                // continue;
                final Map<String, Object> artifact = new HashMap<>();
                artifact.put("id", project.getPathWithNamespace() + "/" + file.getPath());
                artifact.put("text", file.getName());
                artifact.put("children", true);
                artifact.put("icon", file.isDirectory() ? "fa fa-folder" : "fa fa-file-o");
                artifacts.add(artifact);
            }
            if (artifacts.size() == 0)
                for (final DXTaskExecution taskExecution : getSystem().getTaskExecutions(fullPath)
                        .collect(Collectors.toList())) {
                    final Map<String, Object> artifact = new HashMap<>();
                    artifact.put("id", fullPath + "/" + taskExecution.getId());
                    artifact.put("text", taskExecution.getDateInitiated().toString());
                    artifact.put("children", false);
                    artifact.put("icon", "fa fa-cog");
                    artifacts.add(artifact);
                }
        } else {
            while (path.startsWith("@"))
                path = path.substring(1);
            final String projectPath = path.substring(0, path.indexOf("@"));

            path = path.substring(projectPath.length() + 1);
            final String reference = path.substring(0, path.indexOf("@"));

            path = path.substring(reference.length() + 1);

            final DXProject project = getSystem().getProject(projectPath);
            for (final DXFile file : project.getRepository().getCommit(reference).getFiles(path, false)
                    .collect(Collectors.toList())) {
                // if (!file.isDirectory())
                // continue;
                final Map<String, Object> artifact = new HashMap<>();
                artifact.put("id", "@" + projectPath + "@" + reference + "@" + file.getPath());
                artifact.put("text", file.getName());
                artifact.put("children", true);
                artifact.put("icon", file.isDirectory() ? "fa fa-folder" : "fa fa-file-o");
                artifacts.add(artifact);
            }
            if (artifacts.size() == 0)
                for (final DXTaskExecution taskExecution : getSystem().getTaskExecutions(fullPath)
                        .collect(Collectors.toList())) {
                    final Map<String, Object> artifact = new HashMap<>();
                    artifact.put("id", fullPath + "/" + taskExecution.getId());
                    artifact.put("text", taskExecution.getDateInitiated().toString());
                    artifact.put("children", false);
                    artifact.put("icon", "fa fa-cog");
                    artifacts.add(artifact);
                }
        }
        return artifacts;
    }

    @GET
    @Path("studies")
    public Viewable getStudies(@QueryParam("action") final String action)
    {
        return new Viewable("studies", this);
    }

    @GET
    @Path("studies/{id}")
    public Viewable getStudy(@PathParam("id") final String id, @QueryParam("action") final String action)
    {
        return new Viewable("study", this);
    }

    @GET
    @Path("types.json")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, Object>> getTypesTree(@QueryParam("path") final String path) throws DXException
    {
        final List<Map<String, Object>> artifacts = new LinkedList<>();
        final UnityDXBranch branch = getBranch();
        if (branch == null)
            return artifacts;
        final UnityDXOntology ontology = branch.getOntology();
        List<OntClass> types;
        if ("#".equals(path))
            types = ontology.getArtifactTypes();
        else
            types = ontology.getArtifactTypes(path);
        for (final OntClass type : types) {
            final Map<String, Object> artifact = new HashMap<>();
            artifact.put("id", type.getURI());
            artifact.put("text", type.getLocalName());
            artifact.put("children", true);
            artifact.put("icon", "fa fa-cube");
            artifacts.add(artifact);
        }

        return artifacts;
    }

    public static <T> T[] concat(T[] first, T[] second)
    {
        final T[] result = Arrays.copyOf(first, first.length + second.length);
        java.lang.System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
