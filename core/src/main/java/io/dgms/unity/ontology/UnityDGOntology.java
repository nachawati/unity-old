/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.ontology;

import java.util.stream.Stream;

import com.google.gson.JsonObject;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGBranch;
import io.dgms.unity.api.DGFile;
import io.dgms.unity.api.DGOntology;
import io.dgms.unity.api.DGProject;
import io.dgms.unity.api.DGResource;
import io.dgms.unity.repository.UnityDGBranch;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGOntology extends UnityDGSessionObject implements DGOntology
{
    /**
     *
     */
    private final UnityDGBranch branch;

    /**
     * @param session
     * @param branch
     */
    public UnityDGOntology(UnityDGSession session, UnityDGBranch branch)
    {
        super(session);
        this.branch = branch;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getBranch()
     */
    @Override
    public DGBranch getBranch()
    {
        return branch;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getFiles(java.lang.String)
     */
    @Override
    public Stream<? extends DGFile> getFiles(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getProject()
     */
    @Override
    public DGProject getProject()
    {
        return branch.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getResource(java.lang.String)
     */
    @Override
    public JsonObject getResource(String identifier)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getResources(java.lang.String)
     */
    @Override
    public Stream<? extends DGResource> getResources(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
