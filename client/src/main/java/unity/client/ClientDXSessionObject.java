/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import java.io.IOException;

import javax.ws.rs.client.WebTarget;

import org.gitlab4j.api.GitLabApi;

import unity.api.DXSessionObject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public abstract class ClientDXSessionObject implements DXSessionObject
{
    /**
     *
     */
    protected ClientDXSession session;

    /**
     *
     */
    protected ClientDXSessionObject()
    {
    }

    /**
     * @param session
     */
    public ClientDXSessionObject(ClientDXSession session)
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
     * @see unity.api.DXSessionObject#getSession()
     */
    @Override
    public ClientDXSession getSession()
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
