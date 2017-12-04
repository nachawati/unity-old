/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import org.gitlab4j.api.models.Branch;

import unity.api.DXBranch;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXBranch extends ClientDXCommit implements DXBranch
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
    public ClientDXBranch(ClientDXSession session, ClientDXRepository repository, Branch object)
    {
        super(session, repository, object.getCommit());
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXBranch#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXBranch#getOntology()
     */
    @Override
    public ClientDXOntology getOntology()
    {
        return null;
    }
}
