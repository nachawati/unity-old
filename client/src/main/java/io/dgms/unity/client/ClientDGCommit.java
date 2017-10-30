/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

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

import io.dgms.unity.api.DGCommit;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSystem;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGCommit extends ClientDGSessionObject implements DGCommit
{
    /**
     *
     */
    private final Commit             object;

    /**
     *
     */
    private final ClientDGRepository repository;

    /**
     * @param session
     * @param repository
     * @param object
     */
    public ClientDGCommit(ClientDGSession session, ClientDGRepository repository, Commit object)
    {
        super(session);
        this.repository = repository;
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getAsPackageReference()
     */
    @Override
    public ClientDGPackageReference getAsPackageReference()
    {
        return new ClientDGPackageReference("@" + getProject().getPathWithNamespace(), getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getAuthorEmail()
     */
    @Override
    public String getAuthorEmail()
    {
        return object.getAuthorEmail();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getAuthorName()
     */
    @Override
    public String getAuthorName()
    {
        return object.getAuthorName();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getCommitterEmail()
     */
    @Override
    public String getCommitterEmail()
    {
        return object.getCommitterEmail();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getCommitterName()
     */
    @Override
    public String getCommitterName()
    {
        return object.getCommitterName();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getDateAuthored()
     */
    @Override
    public Instant getDateAuthored()
    {
        return object.getAuthoredDate().toInstant();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getDateCommitted()
     */
    @Override
    public Instant getDateCommitted()
    {
        return object.getCommittedDate().toInstant();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getDateConstructed()
     */
    @Override
    public Instant getDateConstructed()
    {
        return object.getCreatedAt().toInstant();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getDependencies()
     */
    @Override
    public Stream<ClientDGPackageReference> getDependencies() throws DGException
    {
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile("package.json", getProject().getId(),
                    object.getId());
            final JsonObject object = new Gson().fromJson(new String(Base64.getDecoder().decode(file.getContent())),
                    JsonObject.class);
            final JsonObject dependencies = object.getAsJsonObject("dependencies");
            return dependencies.entrySet().stream()
                    .map(d -> new ClientDGPackageReference(d.getKey(), d.getValue().getAsString()));
        } catch (final GitLabApiException e) {
            return Stream.of();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getFile(java.lang.String)
     */
    @Override
    public ClientDGFile getFile(String path) throws DGException
    {
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile(path, repository.getProject().getId(),
                    getId());
            if (file == null)
                throw new DGException();
            return new ClientDGFile(getSession(), this, file);
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getFiles(java.lang.String)
     */
    @Override
    public Stream<ClientDGFile> getFiles(String path) throws DGException
    {
        try {
            while (path != null && path.startsWith("/"))
                path = path.substring(1);
            return api().getRepositoryApi().getTree(repository.getProject().getId(), path, object.getId()).stream()
                    .filter(f -> f.getName().endsWith(".metadata") || f.getName().endsWith(".metadata.json")
                            || f.getName().equals(".gitkeep") ? false : true)
                    .map(t -> new ClientDGFile(getSession(), this, t));
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getId()
     */
    @Override
    public String getId()
    {
        return object.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getMessage()
     */
    @Override
    public String getMessage()
    {
        return object.getMessage();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getProject()
     */
    @Override
    public ClientDGProject getProject()
    {
        return repository.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getRepository()
     */
    @Override
    public ClientDGRepository getRepository()
    {
        return repository;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String path) throws DGException
    {
        try {
            final RepositoryFile file = api().getRepositoryFileApi().getFile(path, repository.getProject().getId(),
                    getId());
            return new ByteArrayInputStream(Base64.getDecoder().decode(file.getContent()));
        } catch (final Exception e) {
        }
        for (final ClientDGPackageReference dependency : getDependencies().collect(Collectors.toList()))
            try {
                final ClientDGProject project = getSession().getSystem().getProject(dependency.getName());
                final RepositoryFile file = api().getRepositoryFileApi().getFile(path, project.getId(),
                        dependency.getVersion());
                return new ByteArrayInputStream(Base64.getDecoder().decode(file.getContent()));
            } catch (final Exception e) {
            }
        throw new DGException();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getResourceAsStream(java.net.URI)
     */
    @Override
    public InputStream getResourceAsStream(URI namespace) throws DGException
    {
        return getResourceAsStream(DGSystem.toPathString(namespace));
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getShortId()
     */
    @Override
    public String getShortId()
    {
        return object.getShortId();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getStatus()
     */
    @Override
    public String getStatus()
    {
        return object.getStatus();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCommit#getTimestamp()
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
     * @see io.dgms.unity.api.DGCommit#getTitle()
     */
    @Override
    public String getTitle()
    {
        return object.getTitle();
    }
}
