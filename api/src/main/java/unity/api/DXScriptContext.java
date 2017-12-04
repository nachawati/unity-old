/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.script.ScriptContext;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXScriptContext extends AutoCloseable, ScriptContext
{
    /**
     * @param packageReference
     * @throws DXException
     */
    void addPackage(DXPackageReference packageReference) throws DXException;

    /**
     * @param path
     * @throws DXException
     */
    void addPackage(Path path) throws DXException;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    void close();

    /**
     * @param path
     * @return InputStream
     * @throws IOException
     */
    InputStream getResourceAsStream(String path) throws IOException;
}
