/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import java.io.InputStream;
import java.util.stream.Stream;

import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.api.models.TreeItem;

import unity.api.DXException;
import unity.api.DXFile;
import unity.api.DXResource;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXFile extends ClientDXSessionObject implements DXFile
{
    /**
     *
     */
    private final ClientDXCommit commit;

    /**
     *
     */
    final Object                 object;

    /**
     * @param session
     * @param commit
     * @param object
     */
    public ClientDXFile(ClientDXSession session, ClientDXCommit commit, RepositoryFile object)
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
    public ClientDXFile(ClientDXSession session, ClientDXCommit commit, TreeItem object)
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
    public ClientDXCommit getCommit()
    {
        return commit;
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
    public ClientDXProject getProject()
    {
        return commit.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getRepository()
     */
    @Override
    public ClientDXRepository getRepository()
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
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXFile#getType()
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
    public InputStream newInputStream() throws DXException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
