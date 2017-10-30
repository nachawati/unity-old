/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGMember extends DGSessionObject
{
    /**
     * @return DGAccessLevel
     */
    DGAccessLevel getAccessLevel();

    /**
     * @return String
     */
    String getEmail();

    /**
     * @return Integer
     */
    Integer getId();

    /**
     * @return String
     */
    String getName();

    /**
     * @return String
     */
    String getState();

    /**
     * @return DGUser
     */
    DGUser getUser();

    /**
     * @return String
     */
    String getUsername();
}
