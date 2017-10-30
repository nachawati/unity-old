/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import org.gitlab4j.api.models.Tag;

import io.dgms.unity.api.DGTag;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDGTag extends ClientDGCommit implements DGTag
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
    public ClientDGTag(ClientDGSession session, ClientDGRepository repository, Tag object)
    {
        super(session, repository, object.getCommit());
        this.object = object;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTag#getDescription()
     */
    @Override
    public String getDescription()
    {
        return object.getRelease().getDescription();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTag#getName()
     */
    @Override
    public String getName()
    {
        return object.getName();
    }
}
