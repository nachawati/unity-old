/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.system;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import org.gitlab4j.api.models.Project;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGProject;
import io.dgms.unity.api.DGTaskExecution;
import io.dgms.unity.api.DGVisibility;
import io.dgms.unity.repository.UnityDGRepository;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGProject extends UnityDGSessionObject implements DGProject
{
    /**
     *
     */
    private final Project object;

    /**
     * @param session
     * @param object
     */
    public UnityDGProject(UnityDGSession session, Project object)
    {
        super(session);
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getArchived()
     */
    @Override
    public Boolean getArchived()
    {
        return object.getArchived();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getDateConstructed()
     */
    @Override
    public Instant getDateInstantiated()
    {
        if (object.getCreatedAt() != null)
            return object.getCreatedAt().toInstant();
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getDefaultBranchName()
     */
    @Override
    public String getDefaultBranchName()
    {
        return object.getDefaultBranch();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getDescription()
     */
    @Override
    public String getDescription()
    {
        return object.getDescription();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getId()
     */
    @Override
    public Integer getId()
    {
        return object.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getImageUrl()
     */
    @Override
    public String getImageUrl()
    {
        return object.getAvatarUrl();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getLastActivityDate()
     */
    @Override
    public Instant getLastActivityDate()
    {
        if (object.getLastActivityAt() != null)
            return object.getLastActivityAt().toInstant();
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getNamespace()
     */
    @Override
    public String getNamespace()
    {
        return object.getNamespace().getFullPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getNameWithNamespace()
     */
    @Override
    public String getNameWithNamespace()
    {
        return object.getNameWithNamespace();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getPath()
     */
    @Override
    public String getPath()
    {
        return object.getPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getPathWithNamespace()
     */
    @Override
    public String getPathWithNamespace()
    {
        return object.getPathWithNamespace();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getRepository()
     */
    @Override
    public UnityDGRepository getRepository()
    {
        return new UnityDGRepository(getSession(), this);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getSshUrlToRepo()
     */
    @Override
    public String getSshUrlToRepo()
    {
        return object.getSshUrlToRepo();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getStarCount()
     */
    @Override
    public Integer getStarCount()
    {
        return object.getStarCount();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getTagList()
     */
    @Override
    public List<String> getTagList()
    {
        return object.getTagList();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getTasks(io.dgms.unity.api.DGTaskStatus)
     */
    @Override
    public Stream<? extends DGTaskExecution> getTaskExecutions() throws DGException
    {
        return getSession().getSystem().getTaskExecutions();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getVisibility()
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

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getVisibilityLevel()
     */
    @Override
    public Integer getVisibilityLevel()
    {
        return object.getVisibilityLevel();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#getWorkspace()
     */
    @Override
    public UnityDGWorkspace getWorkspace()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGProject#isPublic()
     */
    @Override
    public Boolean isPublic()
    {
        return object.getPublic();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return object.getNameWithNamespace();
    }
}
