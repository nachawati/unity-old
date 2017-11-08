/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.engines.basex;

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
import org.basex.query.QueryProcessor;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGScriptEngine;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.modules.languages.jsoniq.compiler.translator.JSONiqTranslator;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class BaseXDGScriptEngine implements DGScriptEngine
{
    /**
     *
     */
    private BaseXDGScriptContext             context;

    /**
     *
     */
    private final BaseXDGScriptContext       defaultContext;

    /**
     *
     */
    private final BaseXDGScriptEngineFactory factory;

    /**
     *
     */
    private final DGSession                  session;

    /**
     *
     */
    private final JSONiqTranslator           translator;

    /**
     * @param factory
     */
    public BaseXDGScriptEngine(BaseXDGScriptEngineFactory factory)
    {
        this.factory = factory;
        session = null;
        defaultContext = new BaseXDGScriptContext(this);
        translator = new JSONiqTranslator(JSONiqTranslator.Target.XQUERY31);
    }

    /**
     * @param factory
     * @param session
     */
    public BaseXDGScriptEngine(BaseXDGScriptEngineFactory factory, DGSession session)
    {
        this.factory = factory;
        this.session = session;
        defaultContext = new BaseXDGScriptContext(this, session);
        translator = new JSONiqTranslator(JSONiqTranslator.Target.XQUERY31);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptEngine#addPackage(io.dgms.unity.api.
     * DGPackageReference)
     */
    @Override
    public void addPackage(DGPackageReference packageReference) throws DGException
    {
        getContext().addPackage(packageReference);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptEngine#addPackage(java.nio.file.Path)
     */
    @Override
    public void addPackage(Path path) throws DGException
    {
        getContext().addPackage(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptEngine#close()
     */
    @Override
    public void close()
    {
        defaultContext.close();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptEngine#compile(java.io.Reader)
     */
    @Override
    public BaseXDGCompiledScript compile(Reader script) throws ScriptException
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
     * @see io.dgms.unity.api.DGScriptEngine#compile(java.lang.String)
     */
    @Override
    public BaseXDGCompiledScript compile(String script) throws ScriptException
    {
        final QueryProcessor processor = new QueryProcessor(translate(script), getContext().context);
        processor.uriResolver(getContext());
        return new BaseXDGCompiledScript(this, processor);
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
                ((BaseXDGScriptContext) context).context)) {
            processor.uriResolver((BaseXDGScriptContext) context);
            return processor.value().toJava();
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
    public BaseXDGScriptContext getContext()
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
     * @see io.dgms.unity.api.DGScriptEngine#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String path) throws IOException
    {
        return getContext().getResourceAsStream(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptEngine#getSession()
     */
    @Override
    public DGSession getSession()
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
        this.context = (BaseXDGScriptContext) context;
    }

    /**
     * @param script
     * @return
     */
    protected String translate(String script)
    {
        try {
            System.out.println(translator.translate(script));
            return translator.translate(script);
        } catch (final DGException e) {
            return script;
        }
    }
}
