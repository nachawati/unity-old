/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.time.Instant;
import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXUser extends DXSessionObject
{
    /**
     * @param title
     * @param publicKey
     * @throws DXException
     */
    void addSshKey(String title, String publicKey) throws DXException;

    /**
     * @return Instant
     */
    Instant getCurrentSignInDate();

    /**
     * @return Instant
     */
    Instant getDateConfirmed();

    /**
     * @return Instant
     */
    Instant getDateInstantiated();

    /**
     * @return String
     */
    String getDescription();

    /**
     * @return String
     */
    String getEmail();

    /**
     * @return Integer
     */
    Integer getId();

    /**
     * @return String
     */
    String getImageUrl();

    /**
     * @return Boolean
     */
    Boolean getIsAdmin();

    /**
     * @return Instant
     */
    Instant getLastActivityDate();

    /**
     * @return Instant
     */
    Instant getLastSignInDate();

    /**
     * @return String
     */
    String getLocation();

    /**
     * @return String
     */
    String getName();

    /**
     * @return String
     */
    String getOrganization();

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
     * @return Integer
     */
    Integer getProjectsLimit();

    /**
     * @return String
     */
    String getState();

    /**
     * @return String
     */
    String getUsername();

    /**
     * @return String
     */
    String getWebsiteUrl();
}
