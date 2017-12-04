/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.system;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import org.gitlab4j.api.models.Project;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXException;
import unity.api.DXProject;
import unity.api.DXTaskExecution;
import unity.api.DXVisibility;
import unity.repository.UnityDXRepository;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXProject extends UnityDXSessionObject implements DXProject
{
    /**
     *
     */
    private final Project object;

    /**
     * @param session
     * @param object
     */
    public UnityDXProject(UnityDXSession session, Project object)
    {
        super(session);
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getArchived()
     */
    @Override
    public Boolean getArchived()
    {
        return object.getArchived();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getDateConstructed()
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
     * @see unity.api.DXProject#getDefaultBranchName()
     */
    @Override
    public String getDefaultBranchName()
    {
        return object.getDefaultBranch();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getDescription()
     */
    @Override
    public String getDescription()
    {
        return object.getDescription();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getId()
     */
    @Override
    public Integer getId()
    {
        return object.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getImageUrl()
     */
    @Override
    public String getImageUrl()
    {
        return object.getAvatarUrl();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getLastActivityDate()
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
     * @see unity.api.DXProject#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getNamespace()
     */
    @Override
    public String getNamespace()
    {
        return object.getNamespace().getFullPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getNameWithNamespace()
     */
    @Override
    public String getNameWithNamespace()
    {
        return object.getNameWithNamespace();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getPath()
     */
    @Override
    public String getPath()
    {
        return object.getPath();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getPathWithNamespace()
     */
    @Override
    public String getPathWithNamespace()
    {
        return object.getPathWithNamespace();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getRepository()
     */
    @Override
    public UnityDXRepository getRepository()
    {
        return new UnityDXRepository(getSession(), this);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getSshUrlToRepo()
     */
    @Override
    public String getSshUrlToRepo()
    {
        return object.getSshUrlToRepo();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getStarCount()
     */
    @Override
    public Integer getStarCount()
    {
        return object.getStarCount();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getTagList()
     */
    @Override
    public List<String> getTagList()
    {
        return object.getTagList();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getTasks(unity.api.DXTaskStatus)
     */
    @Override
    public Stream<? extends DXTaskExecution> getTaskExecutions() throws DXException
    {
        return getSession().getSystem().getTaskExecutions();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getVisibility()
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

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getVisibilityLevel()
     */
    @Override
    public Integer getVisibilityLevel()
    {
        return object.getVisibilityLevel();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#getWorkspace()
     */
    @Override
    public UnityDXWorkspace getWorkspace()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXProject#isPublic()
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
