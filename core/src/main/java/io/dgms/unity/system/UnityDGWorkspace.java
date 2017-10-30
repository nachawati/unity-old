/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.system;

import java.util.stream.Stream;

import org.gitlab4j.api.models.Group;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGVisibility;
import io.dgms.unity.api.DGWorkspace;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGWorkspace extends UnityDGSessionObject implements DGWorkspace
{
    /**
     *
     */
    private final Group object;

    /**
     * @param session
     * @param object
     */
    public UnityDGWorkspace(UnityDGSession session, Group object)
    {
        super(session);
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getDescription()
     */
    @Override
    public String getDescription()
    {
        return object.getDescription();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getFullName()
     */
    @Override
    public String getFullName()
    {
        return object.getFullName();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getFullPath()
     */
    @Override
    public String getFullPath()
    {
        return object.getFullPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getId()
     */
    @Override
    public Integer getId()
    {
        return object.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getImageUrl()
     */
    @Override
    public String getImageUrl()
    {
        return object.getAvatarUrl();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getParent()
     */
    @Override
    public UnityDGWorkspace getParent()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getParentId()
     */
    @Override
    public Integer getParentId()
    {
        return object.getParentId();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getPath()
     */
    @Override
    public String getPath()
    {
        return object.getPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getProject(java.lang.String)
     */
    @Override
    public UnityDGProject getProject(String path) throws DGException
    {
        return getSession().getSystem().getProject(object.getFullPath() + "/" + path);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getProjects()
     */
    @Override
    public Stream<UnityDGProject> getProjects() throws DGException
    {
        try {
            return api().getProjectApi().getProjects().stream()
                    .flatMap(p -> Stream.of(new UnityDGProject(getSession(), p)));
        } catch (final Exception e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getSubWorkspace(java.lang.String)
     */
    @Override
    public UnityDGWorkspace getSubWorkspace(String path) throws DGException
    {
        return getSession().getSystem().getWorkspace(object.getFullPath() + "/" + path);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getSubWorkspaces()
     */
    @Override
    public Stream<UnityDGWorkspace> getSubWorkspaces() throws DGException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGWorkspace#getVisibility()
     */
    @Override
    public DGVisibility getVisibility()
    {
        switch (object.getVisibility()) {
        case INTERNAL:
            return DGVisibility.INTERNAL;
        case PRIVATE:
            return DGVisibility.PRIVATE;
        case PUBLIC:
            return DGVisibility.PUBLIC;
        default:
            return null;
        }
    }
}
