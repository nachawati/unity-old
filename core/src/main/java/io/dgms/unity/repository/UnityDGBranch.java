/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.repository;

import org.gitlab4j.api.models.Branch;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.api.DGBranch;
import io.dgms.unity.api.DGOntology;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGBranch extends UnityDGCommit implements DGBranch
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
    public UnityDGBranch(UnityDGSession session, UnityDGRepository repository, Branch object)
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
    public DGOntology getOntology()
    {
        return null;
    }
}
