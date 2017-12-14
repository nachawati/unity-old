/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import java.util.stream.Stream;

import org.gitlab4j.api.models.Group;

import unity.api.DXVisibility;
import unity.api.DXWorkspace;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXWorkspace extends ClientDXSessionObject implements DXWorkspace
{
    /**
     *
     */
    private final Group object;

    /**
     * @param session
     * @param object
     */
    public ClientDXWorkspace(ClientDXSession session, Group object)
    {
        super(session);
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getDescription()
     */
    @Override
    public String getDescription()
    {
        return object.getDescription();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getFullName()
     */
    @Override
    public String getFullName()
    {
        return object.getFullName();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getFullPath()
     */
    @Override
    public String getFullPath()
    {
        return object.getFullPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getId()
     */
    @Override
    public Integer getId()
    {
        return object.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getImageUrl()
     */
    @Override
    public String getImageUrl()
    {
        return object.getAvatarUrl();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getParent()
     */
    @Override
    public ClientDXWorkspace getParent()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getParentId()
     */
    @Override
    public Integer getParentId()
    {
        return object.getParentId();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getPath()
     */
    @Override
    public String getPath()
    {
        return object.getPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getProject(java.lang.String)
     */
    @Override
    public ClientDXProject getProject(String path)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getProjects()
     */
    @Override
    public Stream<ClientDXProject> getProjects()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getSubWorkspace(java.lang.String)
     */
    @Override
    public ClientDXWorkspace getSubWorkspace(String path)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getSubWorkspaces()
     */
    @Override
    public Stream<ClientDXWorkspace> getSubWorkspaces()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getVisibility()
     */
    @Override
    public DXVisibility getVisibility()
    {
        switch (object.getVisibility()) {
        case INTERNAL:
            return DXVisibility.INTERNAL;
        case PRIVATE:
            return DXVisibility.PRIVATE;
        case PUBLIC:
            return DXVisibility.PUBLIC;
        default:
            return null;
        }
    }
}
