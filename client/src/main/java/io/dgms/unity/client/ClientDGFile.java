/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import java.io.InputStream;

import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.api.models.TreeItem;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGFile;
import io.dgms.unity.api.DGResource;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGFile extends ClientDGSessionObject implements DGFile
{
    /**
     *
     */
    private final ClientDGCommit commit;

    /**
     *
     */
    final Object                 object;

    /**
     * @param session
     * @param commit
     * @param object
     */
    public ClientDGFile(ClientDGSession session, ClientDGCommit commit, RepositoryFile object)
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
    public ClientDGFile(ClientDGSession session, ClientDGCommit commit, TreeItem object)
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
    public ClientDGCommit getCommit()
    {
        return commit;
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
    public ClientDGProject getProject()
    {
        return commit.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGFile#getRepository()
     */
    @Override
    public ClientDGRepository getRepository()
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
    public InputStream newInputStream() throws DGException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
