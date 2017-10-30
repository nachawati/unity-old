/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.script.ScriptContext;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGScriptContext extends AutoCloseable, ScriptContext
{
    /**
     * @param packageReference
     * @throws DGException
     */
    void addPackage(DGPackageReference packageReference) throws DGException;

    /**
     * @param path
     * @throws DGException
     */
    void addPackage(Path path) throws DGException;

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
