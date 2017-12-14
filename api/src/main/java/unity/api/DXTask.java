/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.time.Instant;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXTask
{
    /**
     * @return Instant
     */
    Instant getDateSubmitted();

    /**
     * @return Long
     */
    Long getId();

    /**
     * @return String
     */
    String getName();

    /**
     * @return DXPackageReference
     */
    DXPackageReference getPackageReference();

    /**
     * @return String
     */
    String getScript();

    /**
     * @return String
     */
    String getStandardInput();

    /**
     * @return DXTaskStatus
     */
    DXTaskStatus getStatus();

    /**
     * @return Integer
     */
    Integer getUserId();
}
