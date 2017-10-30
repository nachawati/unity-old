/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.time.Instant;
import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGUser extends DGSessionObject
{
    /**
     * @param title
     * @param publicKey
     * @throws DGException
     */
    void addSshKey(String title, String publicKey) throws DGException;

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
    Instant getDateConstructed();

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
