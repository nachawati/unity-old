/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.RepositoryFile;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import unity.api.DXCommit;
import unity.api.DXException;
import unity.api.DXFile;
import unity.api.DXSystem;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXCommit extends ClientDXSessionObject implements DXCommit
{
    /**
     *
     */
    private final Commit             object;

    /**
     *
     */
    private final ClientDXRepository repository;

    /**
     * @param session
     * @param repository
     * @param object
     */
    public ClientDXCommit(ClientDXSession session, ClientDXRepository repository, Commit object)
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
    public ClientDXPackageReference getAsPackageReference()
    {
        return new ClientDXPackageReference("@" + getProject().getPathWithNamespace(), getId());
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
    public Stream<ClientDXPackageReference> getDependencies() throws DXException
    {
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile("package.json", getProject().getId(),
                    object.getId());
            final JsonObject object = new Gson().fromJson(new String(Base64.getDecoder().decode(file.getContent())),
                    JsonObject.class);
            final JsonObject dependencies = object.getAsJsonObject("dependencies");
            return dependencies.entrySet().stream()
                    .map(d -> new ClientDXPackageReference(d.getKey(), d.getValue().getAsString()));
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
    public ClientDXFile getFile(String path) throws DXException
    {
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile(path, repository.getProject().getId(),
                    getId());
            if (file == null)
                throw new DXException();
            return new ClientDXFile(getSession(), this, file);
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }

    @Override
    public Stream<? extends DXFile> getFiles(boolean recursive)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getFiles(java.lang.String)
     */
    @Override
    public Stream<ClientDXFile> getFiles(String path, boolean recursive)
    {
        try {
            while (path != null && path.startsWith("/"))
                path = path.substring(1);
            return api().getRepositoryApi().getTree(repository.getProject().getId(), path, object.getId()).stream()
                    .filter(f -> f.getName().endsWith(".metadata") || f.getName().endsWith(".metadata.jsonld")
                            || f.getName().equals(".gitkeep") ? false : true)
                    .map(t -> new ClientDXFile(getSession(), this, t));
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

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getProject()
     */
    @Override
    public ClientDXProject getProject()
    {
        return repository.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXCommit#getRepository()
     */
    @Override
    public ClientDXRepository getRepository()
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
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile(path, repository.getProject().getId(),
                    getId());
            return new ByteArrayInputStream(Base64.getDecoder().decode(file.getContent()));
        } catch (final Exception e) {
        }
        for (final ClientDXPackageReference dependency : getDependencies().collect(Collectors.toList()))
            try {
                final ClientDXProject project = getSession().getSystem().getProject(dependency.getName());
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
