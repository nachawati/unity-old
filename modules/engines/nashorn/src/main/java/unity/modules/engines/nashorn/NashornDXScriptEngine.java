/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.nashorn;

import static jdk.nashorn.internal.runtime.Source.sourceFor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.commons.io.IOUtils;
import org.basex.query.QueryProcessor;

import de.undercouch.vertx.lang.typescript.TypeScriptClassLoader;
import de.undercouch.vertx.lang.typescript.cache.InMemoryCache;
import de.undercouch.vertx.lang.typescript.compiler.NodeCompiler;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;
import unity.api.DXException;
import unity.api.DXPackageReference;
import unity.api.DXScriptEngine;
import unity.api.DXSession;
import unity.modules.symbolics.symengine.Basic;
import unity.modules.symbolics.symengine.Expr;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class NashornDXScriptEngine implements DXScriptEngine
{
    /**
     *
     */
    private final NodeCompiler                 compiler = new NodeCompiler();

    /**
     *
     */
    private NashornDXScriptContext             context;

    /**
     *
     */
    private final NashornDXScriptContext       defaultContext;

    /**
     *
     */
    private final ScriptEngine                 engine;

    /**
     *
     */
    private final NashornDXScriptEngineFactory factory;

    /**
     *
     */
    private final DXTypeScriptClassLoader      loader   = new DXTypeScriptClassLoader();

    /**
     *
     */
    private final Options                      options  = new Options("nashorn");

    /**
     *
     */
    private final DXSession                    session;
    {
        options.set("anon.functions", true);
        options.set("parse.only", true);
        options.set("scripting", true);
    }

    /**
     * @param factory
     */
    public NashornDXScriptEngine(NashornDXScriptEngineFactory factory)
    {
        this.factory = factory;
        session = null;
        defaultContext = new NashornDXScriptContext(this);
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.put("variable", new Variable());
        engine.getBindings(ScriptContext.ENGINE_SCOPE).put("__", new SymbolicOperators());
        engine.getBindings(ScriptContext.ENGINE_SCOPE).put("variable", new Variable());
        // engine.getBindings(ScriptContext.ENGINE_SCOPE).put("variable", new
        // Variable());
    }

    /**
     * @param factory
     * @param session
     */
    public NashornDXScriptEngine(NashornDXScriptEngineFactory factory, DXSession session)
    {
        this.factory = factory;
        this.session = session;
        defaultContext = new NashornDXScriptContext(this, session);
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.getBindings(ScriptContext.ENGINE_SCOPE).put("__", new SymbolicOperators());
        engine.getBindings(ScriptContext.ENGINE_SCOPE).put("variable", new Variable());
        engine.put("variable", new Variable());
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
    public NashornDXCompiledScript compile(Reader script) throws ScriptException
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
    public NashornDXCompiledScript compile(String script) throws ScriptException
    {
        final QueryProcessor processor = new QueryProcessor(translate(script), getContext().context);
        processor.uriResolver(getContext());
        return new NashornDXCompiledScript(this, processor);
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
        final ErrorManager errors = new ErrorManager();
        final Context context1 = new Context(options, errors, Thread.currentThread().getContextClassLoader());
        final Source source = sourceFor("abc", translate(script));
        final Parser parser = new Parser(context1.getEnv(), source, errors);
        final NashornDXNodeVisitor visitor = new NashornDXNodeVisitor(source);
        parser.parse().accept(visitor);

        return engine.eval(visitor.toString());
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
    public NashornDXScriptContext getContext()
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
        this.context = (NashornDXScriptContext) context;
    }

    /**
     * @param script
     * @return
     */
    protected String translate(String script)
    {
        try {
            final File file = File.createTempFile("SCRIPT", ".ts");
            try (Writer w = Files.newBufferedWriter(file.toPath())) {
                w.write(script);
            }
            final String contents = compiler.compile(file.getAbsolutePath(), loader);

            file.delete();
            return contents;
        } catch (final Exception e) {
            e.printStackTrace();
            return script;
        }
    }

    /**
    *
    */
    private class DXTypeScriptClassLoader extends TypeScriptClassLoader
    {
        public DXTypeScriptClassLoader()
        {
            super(NashornDXScriptEngine.class.getClassLoader(), compiler, new InMemoryCache());
        }

        /*
         * @Override public Source getSource(String filename, String baseFilename)
         * throws IOException { try (InputStream stream = getResourceAsStream(filename))
         * { return new Source(URI.create(filename), IOUtils.toString(stream,
         * StandardCharsets.UTF_8)); } }
         */

    }

    /**
    *
    */
    public static class SymbolicOperators
    {
        public Expr _add(Expr a, Expr b)
        {
            return a.add(b);
        }

        public Expr _add(Expr a, Number b)
        {
            return a.add(Basic.of(b));
        }

        public Expr _add(Number a, Expr b)
        {
            return Basic.of(a).add(b);
        }

        public Expr _lt(Expr a, Expr b)
        {
            return a.lt(b);
        }

        public Expr _lt(Expr a, Number b)
        {
            return a.lt(Basic.of(b));
        }

        public Expr _lt(Number a, Expr b)
        {
            return Basic.of(a).lt(b);
        }
    }

    private static class Variable implements Function<String, Basic>
    {
        @Override
        public Basic apply(String name)
        {
            return new Basic(name);
        }
    }

}
