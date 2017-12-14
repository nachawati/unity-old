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
public interface DXTag extends DXSessionObject, DXCommit
{
    /**
     * @return String
     */
    String getDescription();

    /**
     * @return String
     */
    String getName();
}
