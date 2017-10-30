/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGProject extends DGSessionObject
{
    /**
     * @return Boolean
     */
    Boolean getArchived();

    /**
     * @return Instant
     */
    Instant getDateConstructed();

    /**
     * @return String
     */
    String getDefaultBranchName();

    /**
     * @return String
     */
    String getDescription();

    /**
     * @return Integer
     */
    Integer getId();

    /**
     * @return String
     */
    String getImageUrl();

    /**
     * @return Instant
     */
    Instant getLastActivityDate();

    /**
     * @return String
     */
    String getName();

    /**
     * @return String
     */
    String getNamespace();

    /**
     * @return String
     */
    String getNameWithNamespace();

    /**
     * @return String
     */
    String getPath();

    /**
     * @return String
     */
    String getPathWithNamespace();

    /**
     * @return DGRepository
     */
    DGRepository getRepository();

    /**
     * @return String
     */
    String getSshUrlToRepo();

    /**
     * @return Integer
     */
    Integer getStarCount();

    /**
     * @return List<String>
     */
    List<String> getTagList();

    /**
     * @param status
     * @return Stream<? extends DGTask>
     * @throws DGException
     */
    Stream<? extends DGTask> getTasks(DGTaskStatus status) throws DGException;

    /**
     * @return DGVisibility
     */
    DGVisibility getVisibility();

    /**
     * @return Integer
     */
    Integer getVisibilityLevel();

    /**
     * @return DGWorkspace
     */
    DGWorkspace getWorkspace();

    /**
     * @return Boolean
     */
    Boolean isPublic();
}
