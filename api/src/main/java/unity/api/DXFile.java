/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.io.InputStream;
import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXFile extends DXSessionObject
{
    /**
     * @return DXCommit
     */
    DXCommit getCommit();

    Stream<? extends DXFile> getFiles(boolean recursive);

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
     * @return DXProject
     */
    DXProject getProject();

    /**
     * @return DXRepository
     */
    DXRepository getRepository();

    /**
     * @return String
     */
    String getSchema();

    /**
     * @return DXResource
     */
    String getType();

    /**
     * @return boolean
     */
    boolean isDirectory();

    /**
     * @return InputStream
     * @throws DXException
     */
    InputStream newInputStream() throws DXException;;
}
