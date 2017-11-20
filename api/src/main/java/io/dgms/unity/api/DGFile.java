/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.io.InputStream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGFile extends DGSessionObject
{
    /**
     * @return DGCommit
     */
    DGCommit getCommit();

    /**
     * @return String
     */
    String getId();

    /**
     * @return String
     */
    String getMode();

    /**
     * @return String
     */
    String getName();

    /**
     * @return String
     */
    String getPath();

    /**
     * @return DGProject
     */
    DGProject getProject();

    /**
     * @return DGRepository
     */
    DGRepository getRepository();

    /**
     * @return String
     */
    String getSchema();

    /**
     * @return DGResource
     */
    String getType();

    /**
     * @return boolean
     */
    boolean isDirectory();

    /**
     * @return InputStream
     * @throws DGException
     */
    InputStream newInputStream() throws DGException;;
}
