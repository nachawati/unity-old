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
public interface DXWorkspace extends DXSessionObject
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
     * @return DXWorkspace
     */
    DXWorkspace getParent();

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
     * @return DXProject
     * @throws DXException
     */
    DXProject getProject(String path) throws DXException;

    /**
     * @return Stream<? extends DXProject>
     * @throws DXException
     */
    Stream<? extends DXProject> getProjects() throws DXException;

    /**
     * @param path
     * @return DXWorkspace
     * @throws DXException
     */
    DXWorkspace getSubWorkspace(String path) throws DXException;

    /**
     * @return Stream<? extends DXWorkspace>
     * @throws DXException
     */
    Stream<? extends DXWorkspace> getSubWorkspaces() throws DXException;

    /**
     * @return DXVisibility
     */
    DXVisibility getVisibility();
}
