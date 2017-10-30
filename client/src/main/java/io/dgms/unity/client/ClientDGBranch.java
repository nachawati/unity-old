/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import org.gitlab4j.api.models.Branch;

import io.dgms.unity.api.DGBranch;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGBranch extends ClientDGCommit implements DGBranch
{
    /**
     *
     */
    private final Branch object;

    /**
     * @param session
     * @param repository
     * @param object
     */
    public ClientDGBranch(ClientDGSession session, ClientDGRepository repository, Branch object)
    {
        super(session, repository, object.getCommit());
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGBranch#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGBranch#getOntology()
     */
    @Override
    public ClientDGOntology getOntology()
    {
        return null;
    }
}
