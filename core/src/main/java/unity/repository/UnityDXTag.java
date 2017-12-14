/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.repository;

import org.gitlab4j.api.models.Tag;

import unity.UnityDXSession;
import unity.api.DXTag;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXTag extends UnityDXCommit implements DXTag
{
    /**
     *
     */
    private final Tag object;

    /**
     * @param session
     * @param repository
     * @param object
     */
    public UnityDXTag(UnityDXSession session, UnityDXRepository repository, Tag object)
    {
        super(session, repository, object.getCommit());
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTag#getDescription()
     */
    @Override
    public String getDescription()
    {
        return object.getRelease().getDescription();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTag#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }
}
