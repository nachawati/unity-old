/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.util.stream.Stream;

import com.google.gson.JsonObject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGOntology
{
    /**
     * @return DGBranch
     */
    DGBranch getBranch();

    /**
     * @param type
     * @return Stream<? extends DGFile>
     */
    Stream<? extends DGFile> getFiles(String type);

    /**
     * @return DGProject
     */
    DGProject getProject();

    /**
     * @param identifier
     * @return JsonObject
     */
    JsonObject getResource(String identifier);

    /**
     * @param type
     * @return Stream<? extends DGResource>
     */
    Stream<? extends DGResource> getResources(String type);
}
