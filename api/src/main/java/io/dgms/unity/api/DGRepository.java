/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGRepository extends DGSessionObject
{
    /**
     * @param branchName
     * @return DGBranch
     * @throws DGException
     */
    DGBranch getBranch(String branchName) throws DGException;

    /**
     * @return Stream<? extends DGBranch>
     * @throws DGException
     */
    Stream<? extends DGBranch> getBranches() throws DGException;

    /**
     * @param commitId
     * @return DGCommit
     * @throws DGException
     */
    DGCommit getCommit(String commitId) throws DGException;

    /**
     * @return DGBranch
     * @throws DGException
     */
    DGBranch getDefaultBranch() throws DGException;

    /**
     * @return DGOntology
     * @throws DGException
     */
    DGOntology getDefaultOntology() throws DGException;

    /**
     * @param branchName
     * @return DGOntology
     * @throws DGException
     */
    DGOntology getOntology(String branchName) throws DGException;

    /**
     * @return DGProject
     */
    DGProject getProject();

    /**
     * @return Stream<? extends DGTag>
     * @throws DGException
     */
    Stream<? extends DGTag> getTags() throws DGException;

    /**
     * @param branchName
     * @throws DGException
     */
    void unprotectBranch(String branchName) throws DGException;
}
