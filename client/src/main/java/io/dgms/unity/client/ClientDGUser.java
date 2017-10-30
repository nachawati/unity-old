/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import java.time.Instant;
import java.util.stream.Stream;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.User;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGUser;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGUser extends ClientDGSessionObject implements DGUser
{
    /**
     *
     */
    private final User object;

    /**
     * @param session
     * @param object
     */
    public ClientDGUser(ClientDGSession session, User object)
    {
        super(session);
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#addSshKey(java.lang.String, java.lang.String)
     */
    @Override
    public void addSshKey(String title, String publicKey) throws DGException
    {
        try {
            api().getUserApi().addSshKey(title, publicKey);
        } catch (final GitLabApiException e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getCurrentSignInDate()
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
     * @see io.dgms.unity.api.DGUser#getDateConfirmed()
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
     * @see io.dgms.unity.api.DGUser#getDateConstructed()
     */
    @Override
    public Instant getDateConstructed()
    {
        if (object.getCreatedAt() != null)
            return object.getCreatedAt().toInstant();
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getDescription()
     */
    @Override
    public String getDescription()
    {
        return object.getBio();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getEmail()
     */
    @Override
    public String getEmail()
    {
        return object.getEmail();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getId()
     */
    @Override
    public Integer getId()
    {
        return object.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getImageUrl()
     */
    @Override
    public String getImageUrl()
    {
        return object.getAvatarUrl();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getIsAdmin()
     */
    @Override
    public Boolean getIsAdmin()
    {
        return object.getIsAdmin();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getLastActivityDate()
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
     * @see io.dgms.unity.api.DGUser#getLastSignInDate()
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
     * @see io.dgms.unity.api.DGUser#getLocation()
     */
    @Override
    public String getLocation()
    {
        return object.getLocation();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getOrganization()
     */
    @Override
    public String getOrganization()
    {
        return object.getOrganization();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getProject(java.lang.String)
     */
    @Override
    public ClientDGProject getProject(String path) throws DGException
    {
        return getSession().getSystem().getProject(getUsername() + "/" + path);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getProjects()
     */
    @Override
    public Stream<ClientDGProject> getProjects() throws DGException
    {
        try {
            return api().getProjectApi().getProjects().stream()
                    .flatMap(p -> Stream.of(new ClientDGProject(getSession(), p)));
        } catch (final Exception e) {
            throw new DGException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getProjectsLimit()
     */
    @Override
    public Integer getProjectsLimit()
    {
        return object.getProjectsLimit();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getState()
     */
    @Override
    public String getState()
    {
        return object.getState();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getUsername()
     */
    @Override
    public String getUsername()
    {
        return object.getUsername();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGUser#getWebsiteUrl()
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
