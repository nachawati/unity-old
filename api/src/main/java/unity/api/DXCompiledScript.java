/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import javax.script.CompiledScript;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public abstract class DXCompiledScript extends CompiledScript implements AutoCloseable
{
    /*
     * (non-Javadoc)
     *
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public abstract void close();

    /*
     * (non-Javadoc)
     *
     * @see javax.script.CompiledScript#getEngine()
     */
    @Override
    public abstract DXScriptEngine getEngine();
}
