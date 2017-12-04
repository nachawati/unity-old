/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.basex;

import javax.script.ScriptContext;
import javax.script.ScriptException;

import org.basex.query.QueryProcessor;
import org.basex.query.value.Value;

import unity.api.DXCompiledScript;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class BaseXDXCompiledScript extends DXCompiledScript
{
    /**
     *
     */
    private final BaseXDXScriptEngine engine;

    /**
     *
     */
    private final QueryProcessor      processor;

    /**
     * @param engine
     * @param processor
     */
    public BaseXDXCompiledScript(BaseXDXScriptEngine engine, QueryProcessor processor)
    {
        this.engine = engine;
        this.processor = processor;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DXCompiledScript#close()
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
            final Value value = processor.value();
            return value.toJava();
        } catch (final Exception e) {
            throw new ScriptException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DXCompiledScript#getEngine()
     */
    @Override
    public BaseXDXScriptEngine getEngine()
    {
        return engine;
    }
}
