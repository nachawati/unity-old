/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import java.util.stream.Stream;

import com.google.gson.JsonObject;

import io.dgms.unity.api.DGOntology;
import io.dgms.unity.api.DGResource;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGOntology extends ClientDGSessionObject implements DGOntology
{
    /**
     *
     */
    private final ClientDGBranch branch;

    /**
     * @param session
     * @param branch
     */
    public ClientDGOntology(ClientDGSession session, ClientDGBranch branch)
    {
        super(session);
        this.branch = branch;
    }

    @Override
    public void close() throws Exception
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getBranch()
     */
    @Override
    public ClientDGBranch getBranch()
    {
        return branch;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGOntology#getFiles(java.lang.String)
     */
    @Override
    public Stream<ClientDGFile> getFiles(String type)
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
    public ClientDGProject getProject()
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
    public Stream<DGResource> getResources(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
