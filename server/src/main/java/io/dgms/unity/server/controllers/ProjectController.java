package io.dgms.unity.server.controllers;

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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.jena.ontology.OntClass;
import org.gitlab4j.api.models.RepositoryFile;
import org.glassfish.jersey.server.mvc.Viewable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.dgms.unity.api.DGCommit;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGFile;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGProject;
import io.dgms.unity.api.DGTaskExecution;
import io.dgms.unity.api.DGTaskExecutionStatus;
import io.dgms.unity.ontology.UnityDGOntology;
import io.dgms.unity.repository.UnityDGBranch;
import io.dgms.unity.server.Controller;

public class ProjectController extends Controller
{
    private final DGCommit  commit;
    private final DGProject project;
    private final String    ref;

    public ProjectController(Controller parent, DGProject project) throws DGException
    {
        super(parent);
        this.project = project;
        commit = project.getRepository().getDefaultBranch();
        ref = project.getDefaultBranchName();
    }

    public ProjectController(Controller parent, DGProject project, DGCommit commit, String ref)
    {
        super(parent);
        this.project = project;
        this.commit = commit;
        this.ref = ref;
    }

    @POST
    @Path("console")
    public String doConsole() throws DGException, IOException
    {
        final String script = IOUtils.toString(getRequest().getInputStream(), StandardCharsets.UTF_8);
        final DGTaskExecution execution = getSystem().submitTask("console", script);

        for (int i = 0; i < 100; i++) {
            final DGTaskExecutionStatus status = getSystem().getTaskExecutionStatus(execution.getId());
            if (status == DGTaskExecutionStatus.ACTIVE || status == DGTaskExecutionStatus.QUEUED) {
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                }
                continue;
            }
            break;
        }

        System.out.println(getSystem().getTaskExecutionResult(execution.getId()));

        return getSystem().getTaskExecutionResult(execution.getId());
    }

    @GET
    @Path("file")
    public String doGetFile(@QueryParam("path") String path) throws DGException, IOException
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
        final DGProject project = getSystem().getProject(workspacePath);
        return IOUtils.toString(project.getRepository().getCommit(reference).getResourceAsStream(path),
                StandardCharsets.UTF_8);
    }

    @GET
    @Path("new")
    public Viewable doGetNewForm(@QueryParam("type") String type) throws DGException
    {
        try (InputStream input = commit.getResourceAsStream(((UnityDGBranch) commit).getOntology().getSchema(type))) {
            final String schema = IOUtils.toString(input, StandardCharsets.UTF_8);
            getRequest().setAttribute("schema", StringEscapeUtils.escapeEcmaScript(schema));
            return new Viewable("new", this);
        } catch (final IOException e) {
            throw new DGException(e);
        }
    }

    @POST
    @Path("new")
    public void doPostNew(@FormParam("name") String name, @FormParam("location") String location,
            @FormParam("type") String type, @FormParam("content") String content) throws DGException
    {
        try {
            final RepositoryFile file = new RepositoryFile();
            file.setContent(content);
            file.setFilePath(location.substring(project.getPathWithNamespace().length()) + "/" + name);
            getBranch().api().getRepositoryFileApi().createFile(file, project.getId(), getBranch().getName(), "New");
            final URI uri = URI.create(getRequest().getContextPath() + "/" + getPath() + "/tree");
            getResponse().sendRedirect(uri.toString());
        } catch (final Exception e) {
            throw new DGException(e);
        }
    }

    @SuppressWarnings("deprecation")
    @POST
    @Path("run")
    public String doPostRun() throws DGException, IOException
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
        java.lang.System.out.println(sb.toString());
        return "TEST";
    }

    public UnityDGBranch getBranch()
    {
        if (commit instanceof UnityDGBranch)
            return (UnityDGBranch) commit;
        return null;
    }

    public DGCommit getCommit()
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

    public DGFile getFile(String path) throws DGException
    {
        if (path.startsWith(commit.getProject().getPathWithNamespace())) {
            path = path.substring(commit.getProject().getPathWithNamespace().length());
            return commit.getFile(path);
        }
        if (path.startsWith("@")) {
            final String[] parts = path.split("@");
            final String project = parts[1];
            final String version = parts[2];
            path = parts[3];
            System.out.println(project + "--" + version + "--" + path);
            return getSession().getSystem().getProject(project).getRepository().getCommit(version).getFile(path);
        }
        throw new DGException("file not found");
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
    public List<Map<String, Object>> getOntologyTree(@QueryParam("path") final String path) throws DGException
    {
        final List<Map<String, Object>> artifacts = new LinkedList<>();
        final UnityDGBranch branch = getBranch();
        if (branch == null)
            return artifacts;
        final UnityDGOntology ontology = branch.getOntology();
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

    public DGProject getProject()
    {
        return project;
    }

    public String getRef()
    {
        return ref;
    }

    @GET
    @Path("tree")
    public Viewable getRepositoryResource(@QueryParam("action") final String action, @QueryParam("path") String path)
    {
        if (action != null)
            return new Viewable("actions/" + action, this);
        final StringBuilder sb1 = new StringBuilder();
        sb1.append("[");
        try {

            String[] pathSegments;
            if (!path.startsWith("@")) {
                pathSegments = path.split("/");
                final StringBuilder sb = new StringBuilder();
                final LinkedHashMap<String, String> pathComponents = new LinkedHashMap<>();
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
                final LinkedHashMap<String, String> pathComponents = new LinkedHashMap<>();
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
            final DGFile file = getFile(path);
            getRequest().setAttribute("file", file);
            try (InputStream input = file.newInputStream()) {
                getRequest().setAttribute("content", IOUtils.toString(input, StandardCharsets.UTF_8));
            } catch (final Exception e) {
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        sb1.append("]");
        getRequest().setAttribute("navComponents", sb1.toString());
        return new Viewable("tree", this);
    }

    @GET
    @Path("tree.json")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, Object>> getRepositoryTree(@Context UriInfo info, @QueryParam("path") String path)
            throws DGException
    {
        final List<Map<String, Object>> artifacts = new LinkedList<>();
        if ("#".equals(path)) {
            Map<String, Object> artifact = new HashMap<>();
            artifact.put("id", project.getPathWithNamespace());
            artifact.put("text", project.getName());
            artifact.put("children", true);
            artifact.put("icon", "octicon octicon-repo");
            artifacts.add(artifact);

            for (final DGPackageReference packageReference : commit.getDependencies().collect(Collectors.toList())) {
                artifact = new HashMap<>();
                artifact.put("id", packageReference.getName() + "@" + packageReference.getVersion() + "@");
                artifact.put("text", packageReference.getName() + " [" + packageReference.getVersion() + "]");
                artifact.put("children", true);
                artifact.put("icon", "octicon octicon-plug");
                artifacts.add(artifact);
            }

        } else if (path.startsWith(project.getPathWithNamespace()))
            for (final DGFile file : commit.getFiles(path.substring(project.getPathWithNamespace().length()), false)
                    .collect(Collectors.toList())) {
                final Map<String, Object> artifact = new HashMap<>();
                artifact.put("id", project.getPathWithNamespace() + "/" + file.getPath());
                artifact.put("text", file.getName());
                artifact.put("children", file.isDirectory());
                artifact.put("icon", file.isDirectory() ? "fa fa-folder" : "fa fa-file-o");
                artifacts.add(artifact);
            }
        else {
            while (path.startsWith("@"))
                path = path.substring(1);
            final String projectPath = path.substring(0, path.indexOf("@"));

            path = path.substring(projectPath.length() + 1);
            final String reference = path.substring(0, path.indexOf("@"));

            path = path.substring(reference.length() + 1);

            final DGProject project = getSystem().getProject(projectPath);
            for (final DGFile file : project.getRepository().getCommit(reference).getFiles(path, false)
                    .collect(Collectors.toList())) {
                final Map<String, Object> artifact = new HashMap<>();
                artifact.put("id", "@" + projectPath + "@" + reference + "@" + file.getPath());
                artifact.put("text", file.getName());
                artifact.put("children", file.isDirectory());
                artifact.put("icon", file.isDirectory() ? "fa fa-folder" : "fa fa-file-o");
                artifacts.add(artifact);
            }
        }
        return artifacts;
    }

    @GET
    @Path("types.json")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, Object>> getTypesTree(@QueryParam("path") final String path) throws DGException
    {
        final List<Map<String, Object>> artifacts = new LinkedList<>();
        final UnityDGBranch branch = getBranch();
        if (branch == null)
            return artifacts;
        final UnityDGOntology ontology = branch.getOntology();
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
