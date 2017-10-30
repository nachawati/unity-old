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
import java.io.Reader;
import java.nio.file.Path;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGScriptEngine extends AutoCloseable, Compilable, ScriptEngine
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

    /*
     * (non-Javadoc)
     *
     * @see javax.script.Compilable#compile(java.io.Reader)
     */
    @Override
    DGCompiledScript compile(Reader script) throws ScriptException;

    /*
     * (non-Javadoc)
     *
     * @see javax.script.Compilable#compile(java.lang.String)
     */
    @Override
    DGCompiledScript compile(String script) throws ScriptException;

    /**
     * @param path
     * @return InputStream
     * @throws IOException
     */
    InputStream getResourceAsStream(String path) throws IOException;

    /**
     * @return DGSession
     */
    DGSession getSession();
}
