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
public interface DGWorkspace extends DGSessionObject
{
    /**
     * @return String
     */
    String getDescription();

    /**
     * @return String
     */
    String getFullName();

    /**
     * @return String
     */
    String getFullPath();

    /**
     * @return Integer
     */
    Integer getId();

    /**
     * @return String
     */
    String getImageUrl();

    /**
     * @return String
     */
    String getName();

    /**
     * @return DGWorkspace
     */
    DGWorkspace getParent();

    /**
     * @return Integer
     */
    Integer getParentId();

    /**
     * @return String
     */
    String getPath();

    /**
     * @param path
     * @return DGProject
     * @throws DGException
     */
    DGProject getProject(String path) throws DGException;

    /**
     * @return Stream<? extends DGProject>
     * @throws DGException
     */
    Stream<? extends DGProject> getProjects() throws DGException;

    /**
     * @param path
     * @return DGWorkspace
     * @throws DGException
     */
    DGWorkspace getSubWorkspace(String path) throws DGException;

    /**
     * @return Stream<? extends DGWorkspace>
     * @throws DGException
     */
    Stream<? extends DGWorkspace> getSubWorkspaces() throws DGException;

    /**
     * @return DGVisibility
     */
    DGVisibility getVisibility();
}
