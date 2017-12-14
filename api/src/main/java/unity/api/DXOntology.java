/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.util.stream.Stream;

import com.google.gson.JsonObject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXOntology extends AutoCloseable
{
    /**
     * @return DXBranch
     */
    DXBranch getBranch();

    /**
     * @param type
     * @return Stream<? extends DXFile>
     */
    Stream<? extends DXFile> getFiles(String type);

    /**
     * @return DXProject
     */
    DXProject getProject();

    /**
     * @param identifier
     * @return JsonObject
     */
    JsonObject getResource(String identifier);

    /**
     * @param type
     * @return Stream<? extends DXResource>
     */
    Stream<? extends DXResource> getResources(String type);
}
