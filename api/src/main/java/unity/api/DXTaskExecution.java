/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.time.Duration;
import java.time.Instant;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXTaskExecution
{
    /**
     * @return Instant
     */
    Instant getDateInitiated();

    /**
     * @return Instant
     */
    Instant getDateTerminated();

    /**
     * @return Duration
     */
    Duration getExecutionDuration();

    /**
     * @return Long
     */
    Long getId();

    /**
     * @return String
     */
    String getResult();

    /**
     * @return String
     */
    String getStandardError();

    /**
     * @return String
     */
    String getStandardOutput();

    /**
     * @return DXTaskExecutionStatus
     */
    DXTaskExecutionStatus getStatus();

    /**
     * @return DXTask
     */
    DXTask getTask();
}
