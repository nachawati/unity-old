/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.repository;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.api.models.TreeItem;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXException;
import unity.api.DXFile;
import unity.system.UnityDXProject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXFile extends UnityDXSessionObject implements DXFile
{
    /**
     *
     */
    private final UnityDXCommit commit;

    /**
     *
     */
    final Object                object;

    /**
     * @param session
     * @param commit
     * @param object
     */
    public UnityDXFile(UnityDXSession session, UnityDXCommit commit, RepositoryFile object)
    {
        super(session);
        this.commit = commit;
        this.object = object;
    }

    /**
     * @param session
     * @param commit
     * @param object
     */
    public UnityDXFile(UnityDXSession session, UnityDXCommit commit, TreeItem object)
    {
        super(session);
        this.commit = commit;
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getCommit()
     */
    @Override
    public UnityDXCommit getCommit()
    {
        return commit;
    }

    @Override
    public Stream<? extends DXFile> getFiles(boolean recursive)
    {
        try {
            String path = getPath();
            while (path != null && path.startsWith("/"))
                path = path.substring(1);
            return api().getRepositoryApi().getTree(getCommit().getProject().getId(), path, commit.getId(), recursive)
                    .stream()
                    .filter(f -> f.getName().endsWith(".metadata") || f.getName().endsWith(".metadata.jsonld")
                            || f.getName().equals(".gitkeep") ? false : true)
                    .map(t -> new UnityDXFile(getSession(), commit, t));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getId()
     */
    @Override
    public String getId()
    {
        if (object instanceof RepositoryFile)
            return ((RepositoryFile) object).getBlobId();
        return ((TreeItem) object).getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getMode()
     */
    @Override
    public String getMode()
    {
        if (object instanceof RepositoryFile)
            return null;
        return ((TreeItem) object).getMode();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getName()
     */
    @Override
    public String getName()
    {
        if (object instanceof RepositoryFile)
            return ((RepositoryFile) object).getFileName();
        return ((TreeItem) object).getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getPath()
     */
    @Override
    public String getPath()
    {
        if (object instanceof RepositoryFile)
            return ((RepositoryFile) object).getFilePath();
        return ((TreeItem) object).getPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getProject()
     */
    @Override
    public UnityDXProject getProject()
    {
        return commit.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getRepository()
     */
    @Override
    public UnityDXRepository getRepository()
    {
        return commit.getRepository();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getSchema()
     */
    @Override
    public String getSchema()
    {
        try (InputStream input = commit
                .getResourceAsStream(((UnityDXBranch) commit).getOntology().getSchema(getType()))) {
            return IOUtils.toString(input, StandardCharsets.UTF_8);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getType()
     */
    @Override
    public String getType()
    {
        return ((UnityDXBranch) commit).getOntology().getType(getPath());
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#isDirectory()
     */
    @Override
    public boolean isDirectory()
    {
        if (object instanceof RepositoryFile)
            return false;
        return ((TreeItem) object).getType() == TreeItem.Type.TREE;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#newInputStream()
     */
    @Override
    public UnityDXFileInputStream newInputStream() throws DXException
    {
        return new UnityDXFileInputStream(this);
    }
}
