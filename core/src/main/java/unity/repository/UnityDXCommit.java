/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.repository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.api.models.TreeItem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXCommit;
import unity.api.DXException;
import unity.api.DXFile;
import unity.api.DXSystem;
import unity.registry.UnityDXPackageReference;
import unity.system.UnityDXProject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXCommit extends UnityDXSessionObject implements DXCommit
{
    /**
     *
     */
    private final Commit            object;

    /**
     *
     */
    private final UnityDXRepository repository;

    /**
     * @param session
     * @param repository
     * @param object
     */
    public UnityDXCommit(UnityDXSession session, UnityDXRepository repository, Commit object)
    {
        super(session);
        this.repository = repository;
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getAsPackageReference()
     */
    @Override
    public UnityDXPackageReference getAsPackageReference()
    {
        return new UnityDXPackageReference(getProject().getPathWithNamespace(), getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getAuthorEmail()
     */
    @Override
    public String getAuthorEmail()
    {
        return object.getAuthorEmail();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getAuthorName()
     */
    @Override
    public String getAuthorName()
    {
        return object.getAuthorName();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getCommitterEmail()
     */
    @Override
    public String getCommitterEmail()
    {
        return object.getCommitterEmail();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getCommitterName()
     */
    @Override
    public String getCommitterName()
    {
        return object.getCommitterName();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getDateAuthored()
     */
    @Override
    public Instant getDateAuthored()
    {
        return object.getAuthoredDate().toInstant();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getDateCommitted()
     */
    @Override
    public Instant getDateCommitted()
    {
        return object.getCommittedDate().toInstant();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getDateConstructed()
     */
    @Override
    public Instant getDateInstantiated()
    {
        return object.getCreatedAt().toInstant();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getDependencies()
     */
    @Override
    public Stream<UnityDXPackageReference> getDependencies() throws DXException
    {
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile("package.json", getProject().getId(),
                    object.getId());
            final JsonObject object = new Gson().fromJson(new String(Base64.getDecoder().decode(file.getContent())),
                    JsonObject.class);
            final JsonObject dependencies = object.getAsJsonObject("dependencies");
            return dependencies.entrySet().stream()
                    .map(d -> new UnityDXPackageReference(d.getKey(), d.getValue().getAsString()));
        } catch (final GitLabApiException e) {
            return Stream.of();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getFile(java.lang.String)
     */
    @Override
    public UnityDXFile getFile(String path) throws DXException
    {
        if (path == null || path.trim().length() == 0) {
            final TreeItem item = new TreeItem();
            item.setName(repository.getProject().getName());
            item.setPath(null);
            item.setType(TreeItem.Type.TREE);
            return new UnityDXFile(getSession(), this, item);
        }
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile(path, repository.getProject().getId(),
                    getId());
            return new UnityDXFile(getSession(), this, file);
        } catch (final Exception e) {
            final TreeItem item = new TreeItem();
            if (path.contains("/"))
                item.setName(path.substring(path.lastIndexOf("/") + 1));
            else
                item.setName(path);
            item.setPath(path);
            item.setType(TreeItem.Type.TREE);
            return new UnityDXFile(getSession(), this, item);
        }
    }

    @Override
    public Stream<? extends DXFile> getFiles(boolean recursive)
    {
        return getFiles(null, recursive);
    }

    @Override
    public Stream<? extends DXFile> getFiles(String path, boolean recursive)
    {
        try {
            while (path != null && path.startsWith("/"))
                path = path.substring(1);
            return api().getRepositoryApi().getTree(repository.getProject().getId(), path, object.getId(), recursive)
                    .stream()
                    .filter(f -> f.getName().endsWith(".metadata") || f.getName().endsWith(".metadata.jsonld")
                            || f.getName().equals(".gitkeep") ? false : true)
                    .map(t -> new UnityDXFile(getSession(), this, t));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getId()
     */
    @Override
    public String getId()
    {
        return object.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getMessage()
     */
    @Override
    public String getMessage()
    {
        return object.getMessage();
    }

    @Override
    public Stream<? extends DXFile> getMetaDataFiles(boolean recursive)
    {
        return getMetaDataFiles(null, recursive);
    }

    @Override
    public Stream<? extends DXFile> getMetaDataFiles(String path, boolean recursive)
    {
        try {
            while (path != null && path.startsWith("/"))
                path = path.substring(1);
            return api().getRepositoryApi().getTree(repository.getProject().getId(), path, object.getId(), recursive)
                    .stream().filter(f -> f.getType() == TreeItem.Type.BLOB && f.getName().endsWith(".metadata.jsonld"))
                    .map(t -> new UnityDXFile(getSession(), this, t));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getProject()
     */
    @Override
    public UnityDXProject getProject()
    {
        return repository.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getRepository()
     */
    @Override
    public UnityDXRepository getRepository()
    {
        return repository;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String path) throws DXException
    {
    	System.out.println("getResourceAsStream>>"+path);
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile(path, repository.getProject().getId(),
                    getId());
            return new ByteArrayInputStream(Base64.getDecoder().decode(file.getContent()));
        } catch (final Exception e) {
        }
        for (final UnityDXPackageReference dependency : getDependencies().collect(Collectors.toList()))
            try {
                String name = dependency.getName();
                while (name.startsWith("@"))
                    name = name.substring(1);
                final UnityDXProject project = getSession().getSystem().getProject(name);
                final RepositoryFile file = api().getRepositoryFileApi().getFile(path, project.getId(),
                        dependency.getVersion());
                return new ByteArrayInputStream(Base64.getDecoder().decode(file.getContent()));
            } catch (final Exception e) {
            }
        throw new DXException();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getResourceAsStream(java.net.URI)
     */
    @Override
    public InputStream getResourceAsStream(URI namespace) throws DXException
    {
        return getResourceAsStream(DXSystem.toPathString(namespace));
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getShortId()
     */
    @Override
    public String getShortId()
    {
        return object.getShortId();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getStatus()
     */
    @Override
    public String getStatus()
    {
        return object.getStatus();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getTimestamp()
     */
    @Override
    public Instant getTimestamp()
    {
        if (object.getTimestamp() != null)
            return object.getTimestamp().toInstant();
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getTitle()
     */
    @Override
    public String getTitle()
    {
        return object.getTitle();
    }
}
