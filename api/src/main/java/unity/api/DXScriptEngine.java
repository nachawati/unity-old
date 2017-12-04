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
import java.io.Reader;
import java.nio.file.Path;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXScriptEngine extends AutoCloseable, Compilable, ScriptEngine
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

    /*
     * (non-Javadoc)
     *
     * @see javax.script.Compilable#compile(java.io.Reader)
     */
    @Override
    DXCompiledScript compile(Reader script) throws ScriptException;

    /*
     * (non-Javadoc)
     *
     * @see javax.script.Compilable#compile(java.lang.String)
     */
    @Override
    DXCompiledScript compile(String script) throws ScriptException;

    /**
     * @param path
     * @return InputStream
     * @throws IOException
     */
    InputStream getResourceAsStream(String path) throws IOException;

    /**
     * @return DXSession
     */
    DXSession getSession();
}
