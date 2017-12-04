/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.io.InputStream;
import java.net.URI;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXCommit extends DXSessionObject
{
    /**
     * @return DXPackageReference
     */
    DXPackageReference getAsPackageReference();

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
     * @return Stream<? extends DXPackageReference>
     * @throws DXException
     */
    Stream<? extends DXPackageReference> getDependencies() throws DXException;

    /**
     * @param path
     * @return DXFile
     * @throws DXException
     */
    DXFile getFile(String path) throws DXException;

    /**
     * @param path
     * @return Stream<? extends DXFile>
     * @throws DXException
     */
    Stream<? extends DXFile> getFiles(boolean recursive);

    Stream<? extends DXFile> getFiles(String path, boolean recursive);

    /**
     * @return String
     */
    String getId();

    /**
     * @return String
     */
    String getMessage();

    /**
     * @return DXProject
     */
    DXProject getProject();

    /**
     * @return DXRepository
     */
    DXRepository getRepository();

    /**
     * @param path
     * @return InputStream
     * @throws DXException
     */
    InputStream getResourceAsStream(String path) throws DXException;

    /**
     * @param namespace
     * @return InputStream
     * @throws DXException
     */
    InputStream getResourceAsStream(URI namespace) throws DXException;

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
