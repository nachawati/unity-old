/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity;

import javax.persistence.Transient;

import org.gitlab4j.api.GitLabApi;

import unity.api.DXSessionObject;
import unity.persistence.UnityDXEntityManager;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public abstract class UnityDXSessionObject implements DXSessionObject
{
    /**
     *
     */
    @Transient
    protected transient UnityDXSession session;

    /**
     *
     */
    protected UnityDXSessionObject()
    {
    }

    /**
     * @param session
     */
    public UnityDXSessionObject(UnityDXSession session)
    {
        this.session = session;
    }

    /**
     * @return
     */
    public GitLabApi api()
    {
        return session.api;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXSessionObject#getSession()
     */
    @Override
    public UnityDXSession getSession()
    {
        return session;
    }

    /**
     * @return
     */
    protected UnityDXEntityManager newEntityManager()
    {
        return new UnityDXEntityManager(UnityDXSession.factory.createEntityManager());
    }
}
