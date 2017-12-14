/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import java.util.stream.Stream;

import com.google.gson.JsonObject;

import unity.api.DXOntology;
import unity.api.DXResource;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXOntology extends ClientDXSessionObject implements DXOntology
{
    /**
     *
     */
    private final ClientDXBranch branch;

    /**
     * @param session
     * @param branch
     */
    public ClientDXOntology(ClientDXSession session, ClientDXBranch branch)
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
     * @see unity.api.DXOntology#getBranch()
     */
    @Override
    public ClientDXBranch getBranch()
    {
        return branch;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXOntology#getFiles(java.lang.String)
     */
    @Override
    public Stream<ClientDXFile> getFiles(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXOntology#getProject()
     */
    @Override
    public ClientDXProject getProject()
    {
        return branch.getProject();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXOntology#getResource(java.lang.String)
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
     * @see unity.api.DXOntology#getResources(java.lang.String)
     */
    @Override
    public Stream<DXResource> getResources(String type)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
