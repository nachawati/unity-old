/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.engines.basex;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import org.basex.query.QueryProcessor;

import io.dgms.unity.api.DGCompiledScript;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class BaseXDGCompiledScript extends DGCompiledScript
{
    /**
     *
     */
    private final BaseXDGScriptEngine engine;

    /**
     *
     */
    private final QueryProcessor      processor;

    /**
     * @param engine
     * @param processor
     */
    public BaseXDGCompiledScript(BaseXDGScriptEngine engine, QueryProcessor processor)
    {
        this.engine = engine;
        this.processor = processor;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCompiledScript#close()
     */
    @Override
    public void close()
    {
        processor.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.CompiledScript#eval(javax.script.ScriptContext)
     */
    @Override
    public Object eval(ScriptContext context) throws ScriptException
    {
        try {
            return processor.value();
        } catch (final Exception e) {
            throw new ScriptException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCompiledScript#getEngine()
     */
    @Override
    public BaseXDGScriptEngine getEngine()
    {
        return engine;
    }
}
