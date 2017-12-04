/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.repository;

import org.gitlab4j.api.models.Branch;

import unity.UnityDXSession;
import unity.api.DXBranch;
import unity.ontology.UnityDXOntology;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXBranch extends UnityDXCommit implements DXBranch
{
    /**
     *
     */
    private final Branch    object;
    private UnityDXOntology ontology;

    /**
     * @param session
     * @param repository
     * @param object
     */
    public UnityDXBranch(UnityDXSession session, UnityDXRepository repository, Branch object)
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
    public synchronized UnityDXOntology getOntology()
    {
        if (ontology != null)
            return null;
        return ontology = new UnityDXOntology(getSession(), this);
    }
}
