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
public interface DXResource extends DXSessionObject
{
    /**
     * @return String
     */
    String getIdentifier();

    /**
     * @return String
     */
    String getLocalName();

    /**
     * @return String
     */
    String getType();
}
