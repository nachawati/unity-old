/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.time.Instant;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGTask
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
     * @return DGPackageReference
     */
    DGPackageReference getPackageReference();

    /**
     * @return String
     */
    String getScript();

    /**
     * @return String
     */
    String getStandardInput();

    /**
     * @return DGTaskStatus
     */
    DGTaskStatus getStatus();

    /**
     * @return Integer
     */
    Integer getUserId();
}
