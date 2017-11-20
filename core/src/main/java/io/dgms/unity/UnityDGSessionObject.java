/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity;

import javax.persistence.Transient;

import org.gitlab4j.api.GitLabApi;

import io.dgms.unity.api.DGSessionObject;
import io.dgms.unity.persistence.UnityDGEntityManager;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public abstract class UnityDGSessionObject implements DGSessionObject
{
    /**
     *
     */
    @Transient
    protected transient UnityDGSession session;

    /**
     *
     */
    protected UnityDGSessionObject()
    {
    }

    /**
     * @param session
     */
    public UnityDGSessionObject(UnityDGSession session)
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
     * @see io.dgms.unity.api.DGSessionObject#getSession()
     */
    @Override
    public UnityDGSession getSession()
    {
        return session;
    }

    /**
     * @return
     */
    protected UnityDGEntityManager newEntityManager()
    {
        return new UnityDGEntityManager(UnityDGSession.factory.createEntityManager());
    }
}
