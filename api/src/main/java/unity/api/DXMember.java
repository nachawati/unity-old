/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXMember extends DXSessionObject
{
    /**
     * @return DXAccessLevel
     */
    DXAccessLevel getAccessLevel();

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
     * @return DXUser
     */
    DXUser getUser();

    /**
     * @return String
     */
    String getUsername();
}
