/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import java.io.IOException;

import javax.ws.rs.client.WebTarget;

import org.gitlab4j.api.GitLabApi;

import io.dgms.unity.api.DGSessionObject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public abstract class ClientDGSessionObject implements DGSessionObject
{
    /**
     *
     */
    protected ClientDGSession session;

    /**
     *
     */
    protected ClientDGSessionObject()
    {
    }

    /**
     * @param session
     */
    public ClientDGSessionObject(ClientDGSession session)
    {
        this.session = session;
    }

    /**
     * @return GitLabApi
     */
    protected GitLabApi api()
    {
        return session.api;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGSessionObject#getSession()
     */
    @Override
    public ClientDGSession getSession()
    {
        return session;
    }

    /**
     * @param path
     * @return WebTarget
     * @throws IOException
     */
    protected WebTarget getTarget(String path) throws IOException
    {
        return session.client.target(getSession().host).path(path).queryParam("private_token",
                getSession().getPrivateToken());
    }
}
