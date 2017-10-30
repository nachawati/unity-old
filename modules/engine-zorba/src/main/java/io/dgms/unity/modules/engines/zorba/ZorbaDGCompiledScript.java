/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.engines.zorba;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import io.dgms.unity.api.DGCompiledScript;
import io.zorba.api.XQuery;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ZorbaDGCompiledScript extends DGCompiledScript
{
    /**
     *
     */
    private final ZorbaDGScriptEngine engine;

    /**
     *
     */
    private final XQuery              query;

    /**
     * @param engine
     * @param query
     */
    public ZorbaDGCompiledScript(ZorbaDGScriptEngine engine, XQuery query)
    {
        this.engine = engine;
        this.query = query;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCompiledScript#close()
     */
    @Override
    public void close()
    {
        if (query != null)
            query.destroy();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.CompiledScript#eval(javax.script.ScriptContext)
     */
    @Override
    public Object eval(ScriptContext context) throws ScriptException
    {
        return query.execute(ZorbaDGScriptEngine.OPTIONS);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGCompiledScript#getEngine()
     */
    @Override
    public ZorbaDGScriptEngine getEngine()
    {
        return engine;
    }
}
