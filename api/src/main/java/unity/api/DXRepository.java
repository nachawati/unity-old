/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXRepository extends DXSessionObject
{
    /**
     * @param branchName
     * @return DXBranch
     * @throws DXException
     */
    DXBranch getBranch(String branchName) throws DXException;

    /**
     * @return Stream<? extends DXBranch>
     * @throws DXException
     */
    Stream<? extends DXBranch> getBranches() throws DXException;

    /**
     * @param commitId
     * @return DXCommit
     * @throws DXException
     */
    DXCommit getCommit(String commitId) throws DXException;

    /**
     * @return DXBranch
     * @throws DXException
     */
    DXBranch getDefaultBranch() throws DXException;

    /**
     * @return DXOntology
     * @throws DXException
     */
    DXOntology getDefaultOntology() throws DXException;

    /**
     * @param branchName
     * @return DXOntology
     * @throws DXException
     */
    DXOntology getOntology(String branchName) throws DXException;

    /**
     * @return DXProject
     */
    DXProject getProject();

    /**
     * @return Stream<? extends DXTag>
     * @throws DXException
     */
    Stream<? extends DXTag> getTags() throws DXException;

    /**
     * @param branchName
     * @throws DXException
     */
    void unprotectBranch(String branchName) throws DXException;
}
