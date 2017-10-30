/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import javax.script.ScriptEngineFactory;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGScriptEngineFactory extends ScriptEngineFactory
{
    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getScriptEngine()
     */
    @Override
    DGScriptEngine getScriptEngine();

    /**
     * @param session
     * @return DGScriptEngine
     */
    DGScriptEngine getScriptEngine(DGSession session);
}
