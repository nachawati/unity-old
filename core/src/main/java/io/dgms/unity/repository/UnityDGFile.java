/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.repository;

import java.io.InputStream;
import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.api.models.TreeItem;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGFile;
import io.dgms.unity.system.UnityDGProject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGFile extends UnityDGSessionObject implements DGFile
{
    /**
     *
     */
    private final UnityDGCommit commit;

    /**
     *
     */
    final Object                object;

    /**
     * @param session
     * @param commit
     * @param object
     */
    public UnityDGFile(UnityDGSession session, UnityDGCommit commit, RepositoryFile object)
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
    public UnityDGFile(UnityDGSession session, UnityDGCommit commit, TreeItem object)
    {
        super(session);
        this.commit = commit;
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGFile#getCommit()
     */
    @Override
    public UnityDGCommit getCommit()
    {
        return commit;
    }

    @Override
    public Stream<? extends DGFile> getFiles(boolean recursive)
    {
        try {
            String path = getPath();
            while (path != null && path.startsWith("/"))
                path = path.substring(1);
            return api().getRepositoryApi().getTree(getCommit().getProject().getId(), path, commit.getId(), recursive)
                    .stream()
                    .filter(f -> f.getName().endsWith(".metadata") || f.getName().endsWith(".metadata.json")
                            || f.getName().equals(".gitkeep") ? false : true)
                    .map(t -> new UnityDGFile(getSession(), commit, t));
        } catch (final GitLabApiException e) {
            return Stream.empty();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGFile#getId()
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
     * @see io.dgms.unity.api.DGFile#getMode()
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
     * @see io.dgms.unity.api.DGFile#getName()
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
     * @see io.dgms.unity.api.DGFile#getPath()
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
     * @see io.dgms.unity.api.DGFile#getProject()
     */
    @Override
    public UnityDGProject getProject()
    {
        return commit.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGFile#getRepository()
     */
    @Override
    public UnityDGRepository getRepository()
    {
        return commit.getRepository();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGFile#getSchema()
     */
    @Override
    public String getSchema()
    {
        // TODO Auto-generated method stub

        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGFile#getType()
     */
    @Override
    public String getType()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGFile#isDirectory()
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
     * @see io.dgms.unity.api.DGFile#newInputStream()
     */
    @Override
    public UnityDGFileInputStream newInputStream() throws DGException
    {
        return new UnityDGFileInputStream(this);
    }
}
