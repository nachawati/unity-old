/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGCommit extends DGSessionObject
{
    /**
     * @return DGPackageReference
     */
    DGPackageReference getAsPackageReference();

    /**
     * @return String
     */
    String getAuthorEmail();

    /**
     * @return String
     */
    String getAuthorName();

    /**
     * @return String
     */
    String getCommitterEmail();

    /**
     * @return String
     */
    String getCommitterName();

    /**
     * @return Instant
     */
    Instant getDateAuthored();

    /**
     * @return Instant
     */
    Instant getDateCommitted();

    /**
     * @return Instant
     */
    Instant getDateInstantiated();

    /**
     * @return Stream<? extends DGPackageReference>
     * @throws DGException
     */
    Stream<? extends DGPackageReference> getDependencies() throws DGException;

    /**
     * @param path
     * @return DGFile
     * @throws DGException
     */
    DGFile getFile(String path) throws DGException;

    /**
     * @param path
     * @return Stream<? extends DGFile>
     * @throws DGException
     */
    Stream<? extends DGFile> getFiles(boolean recursive) throws DGException;

    Stream<? extends DGFile> getFiles(String path, boolean recursive) throws DGException;

    /**
     * @return String
     */
    String getId();

    /**
     * @return String
     */
    String getMessage();

    /**
     * @return DGProject
     */
    DGProject getProject();

    /**
     * @return DGRepository
     */
    DGRepository getRepository();

    /**
     * @param path
     * @return InputStream
     * @throws DGException
     */
    InputStream getResourceAsStream(String path) throws DGException;

    /**
     * @param namespace
     * @return InputStream
     * @throws DGException
     */
    InputStream getResourceAsStream(URI namespace) throws DGException;

    /**
     * @return String
     */
    String getShortId();

    /**
     * @return String
     */
    String getStatus();

    /**
     * @return Instant
     */
    Instant getTimestamp();

    /**
     * @return String
     */
    String getTitle();
}
