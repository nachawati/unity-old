/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.zorba;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import io.zorba.api.XQuery;
import unity.api.DXCompiledScript;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ZorbaDXCompiledScript extends DXCompiledScript
{
    /**
     *
     */
    private final ZorbaDXScriptEngine engine;

    /**
     *
     */
    private final XQuery              query;

    /**
     * @param engine
     * @param query
     */
    public ZorbaDXCompiledScript(ZorbaDXScriptEngine engine, XQuery query)
    {
        this.engine = engine;
        this.query = query;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DXCompiledScript#close()
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
        return query.execute(ZorbaDXScriptEngine.OPTIONS);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DXCompiledScript#getEngine()
     */
    @Override
    public ZorbaDXScriptEngine getEngine()
    {
        return engine;
    }
}
