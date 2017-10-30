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
public interface DGBranch extends DGSessionObject, DGCommit
{
    /**
     * @return String
     */
    String getName();

    /**
     * @return DGOntology
     */
    DGOntology getOntology();
}
