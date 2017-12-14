/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import javax.script.ScriptEngineFactory;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXScriptEngineFactory extends ScriptEngineFactory
{
    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getScriptEngine()
     */
    @Override
    DXScriptEngine getScriptEngine();

    /**
     * @param session
     * @return DXScriptEngine
     */
    DXScriptEngine getScriptEngine(DXSession session);
}
