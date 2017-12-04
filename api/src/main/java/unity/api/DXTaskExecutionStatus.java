/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.io.Serializable;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public enum DXTaskExecutionStatus implements Serializable
{
    ACTIVE, FAILED, FINISHED, INTERRUPTED, KILLED, QUEUED
}
