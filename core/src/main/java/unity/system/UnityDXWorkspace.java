/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.system;

import java.util.stream.Stream;

import org.gitlab4j.api.models.Group;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXException;
import unity.api.DXVisibility;
import unity.api.DXWorkspace;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXWorkspace extends UnityDXSessionObject implements DXWorkspace
{
    /**
     *
     */
    private final Group object;

    /**
     * @param session
     * @param object
     */
    public UnityDXWorkspace(UnityDXSession session, Group object)
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
    public UnityDXWorkspace getParent()
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
    public UnityDXProject getProject(String path) throws DXException
    {
        return getSession().getSystem().getProject(object.getFullPath() + "/" + path);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getProjects()
     */
    @Override
    public Stream<UnityDXProject> getProjects() throws DXException
    {
        try {
            return api().getProjectApi().getProjects().stream()
                    .flatMap(p -> Stream.of(new UnityDXProject(getSession(), p)));
        } catch (final Exception e) {
            throw new DXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getSubWorkspace(java.lang.String)
     */
    @Override
    public UnityDXWorkspace getSubWorkspace(String path) throws DXException
    {
        return getSession().getSystem().getWorkspace(object.getFullPath() + "/" + path);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXWorkspace#getSubWorkspaces()
     */
    @Override
    public Stream<UnityDXWorkspace> getSubWorkspaces() throws DXException
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
