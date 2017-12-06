/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.basex;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.commons.io.IOUtils;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;

import unity.api.DXException;
import unity.api.DXPackageReference;
import unity.api.DXScriptEngine;
import unity.api.DXSession;
import unity.modules.languages.jsoniq.compiler.translator.JSONiqTranslator;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class BaseXDXScriptEngine implements DXScriptEngine
{
    /**
     *
     */
    private BaseXDXScriptContext             context;

    /**
     *
     */
    private final BaseXDXScriptContext       defaultContext;

    /**
     *
     */
    private final BaseXDXScriptEngineFactory factory;

    /**
     *
     */
    private final DXSession                  session;

    /**
     *
     */
    private final JSONiqTranslator           translator;

    /**
     * @param factory
     */
    public BaseXDXScriptEngine(BaseXDXScriptEngineFactory factory)
    {
        this.factory = factory;
        session = null;
        defaultContext = new BaseXDXScriptContext(this);
        translator = new JSONiqTranslator(JSONiqTranslator.Target.XQUERY31);
    }

    /**
     * @param factory
     * @param session
     */
    public BaseXDXScriptEngine(BaseXDXScriptEngineFactory factory, DXSession session)
    {
        this.factory = factory;
        this.session = session;
        defaultContext = new BaseXDXScriptContext(this, session);
        translator = new JSONiqTranslator(JSONiqTranslator.Target.XQUERY31);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngine#addPackage(unity.api. DXPackageReference)
     */
    @Override
    public void addPackage(DXPackageReference packageReference) throws DXException
    {
        getContext().addPackage(packageReference);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngine#addPackage(java.nio.file.Path)
     */
    @Override
    public void addPackage(Path path) throws DXException
    {
        getContext().addPackage(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngine#close()
     */
    @Override
    public void close()
    {
        defaultContext.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngine#compile(java.io.Reader)
     */
    @Override
    public BaseXDXCompiledScript compile(Reader script) throws ScriptException
    {
        try {
            return compile(IOUtils.toString(script));
        } catch (final IOException e) {
            throw new ScriptException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngine#compile(java.lang.String)
     */
    @Override
    public BaseXDXCompiledScript compile(String script) throws ScriptException
    {
        final QueryProcessor processor = new QueryProcessor(translate(script), getContext().context);
        processor.uriResolver(getContext());
        return new BaseXDXCompiledScript(this, processor);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#createBindings()
     */
    @Override
    public Bindings createBindings()
    {
        return new SimpleBindings();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(java.io.Reader)
     */
    @Override
    public Object eval(Reader reader) throws ScriptException
    {
        return eval(reader, getContext());
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(java.io.Reader, javax.script.Bindings)
     */
    @Override
    public Object eval(Reader reader, Bindings n) throws ScriptException
    {
        return eval(reader);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(java.io.Reader,
     * javax.script.ScriptContext)
     */
    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException
    {
        try {
            return eval(IOUtils.toString(reader), context);
        } catch (final IOException e) {
            throw new ScriptException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(java.lang.String)
     */
    @Override
    public Object eval(String script) throws ScriptException
    {
        return eval(script, getContext());
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(java.lang.String, javax.script.Bindings)
     */
    @Override
    public Object eval(String script, Bindings n) throws ScriptException
    {
        return eval(script, getContext());
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#eval(java.lang.String,
     * javax.script.ScriptContext)
     */
    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException
    {
        try (QueryProcessor processor = new QueryProcessor(translate(script),
                ((BaseXDXScriptContext) context).context)) {
            processor.uriResolver((BaseXDXScriptContext) context);
            return processor.value().toJava();
        } catch (final QueryException e) {
            throw new ScriptException(e.getMessage());
        } catch (final Exception e) {
            throw new ScriptException(e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#get(java.lang.String)
     */
    @Override
    public Object get(String key)
    {
        return getContext().getBindings(ScriptContext.ENGINE_SCOPE).get(key);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#getBindings(int)
     */
    @Override
    public Bindings getBindings(int scope)
    {
        return getContext().getBindings(scope);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#getContext()
     */
    @Override
    public BaseXDXScriptContext getContext()
    {
        if (context != null)
            return context;
        return defaultContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#getFactory()
     */
    @Override
    public ScriptEngineFactory getFactory()
    {
        return factory;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngine#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String path) throws IOException
    {
        return getContext().getResourceAsStream(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngine#getSession()
     */
    @Override
    public DXSession getSession()
    {
        return session;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#put(java.lang.String, java.lang.Object)
     */
    @Override
    public void put(String key, Object value)
    {
        getContext().getBindings(ScriptContext.ENGINE_SCOPE).put(key, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#setBindings(javax.script.Bindings, int)
     */
    @Override
    public void setBindings(Bindings bindings, int scope)
    {
        getContext().setBindings(bindings, scope);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngine#setContext(javax.script.ScriptContext)
     */
    @Override
    public void setContext(ScriptContext context)
    {
        this.context = (BaseXDXScriptContext) context;
    }

    /**
     * @param script
     * @return
     */
    protected String translate(String script)
    {
        try {
            return translator.translate(script);
        } catch (final DXException e) {
            return script;
        }
    }
}
