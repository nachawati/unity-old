/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.system;

import java.time.Instant;
import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXException;
import unity.api.DXUser;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXUser extends UnityDXSessionObject implements DXUser
{
    /**
     *
     */
    private final User object;

    /**
     * @param session
     * @param object
     */
    public UnityDXUser(UnityDXSession session, User object)
    {
        super(session);
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#addSshKey(java.lang.String, java.lang.String)
     */
    @Override
    public void addSshKey(String title, String publicKey) throws DXException
    {
        try {
            api().getUserApi().addSshKey(title, publicKey);
        } catch (final GitLabApiException e) {
            throw new DXException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getCurrentSignInDate()
     */
    @Override
    public Instant getCurrentSignInDate()
    {
        if (object.getCurrentSignInAt() != null)
            return object.getCurrentSignInAt().toInstant();
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getDateConfirmed()
     */
    @Override
    public Instant getDateConfirmed()
    {
        if (object.getConfirmedAt() != null)
            return object.getConfirmedAt().toInstant();
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getDateConstructed()
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
     * @see unity.api.DXUser#getDescription()
     */
    @Override
    public String getDescription()
    {
        return object.getBio();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getEmail()
     */
    @Override
    public String getEmail()
    {
        return object.getEmail();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getId()
     */
    @Override
    public Integer getId()
    {
        return object.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getImageUrl()
     */
    @Override
    public String getImageUrl()
    {
        return object.getAvatarUrl();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getIsAdmin()
     */
    @Override
    public Boolean getIsAdmin()
    {
        return object.getIsAdmin();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getLastActivityDate()
     */
    @Override
    public Instant getLastActivityDate()
    {
        if (object.getLastActivityOn() != null)
            return object.getLastActivityOn().toInstant();
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getLastSignInDate()
     */
    @Override
    public Instant getLastSignInDate()
    {
        if (object.getLastSignInAt() != null)
            return object.getLastSignInAt().toInstant();
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getLocation()
     */
    @Override
    public String getLocation()
    {
        return object.getLocation();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getOrganization()
     */
    @Override
    public String getOrganization()
    {
        return object.getOrganization();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getProject(java.lang.String)
     */
    @Override
    public UnityDXProject getProject(String path) throws DXException
    {
        return getSession().getSystem().getProject(getUsername() + "/" + path);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getProjects()
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
     * @see unity.api.DXUser#getProjectsLimit()
     */
    @Override
    public Integer getProjectsLimit()
    {
        return object.getProjectsLimit();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getState()
     */
    @Override
    public String getState()
    {
        return object.getState();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getUsername()
     */
    @Override
    public String getUsername()
    {
        return object.getUsername();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXUser#getWebsiteUrl()
     */
    @Override
    public String getWebsiteUrl()
    {
        return object.getWebsiteUrl();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return object.getName();
    }
}
