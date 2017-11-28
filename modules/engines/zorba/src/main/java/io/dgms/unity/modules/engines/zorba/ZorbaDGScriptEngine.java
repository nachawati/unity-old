/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.engines.zorba;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGScriptEngine;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.modules.languages.jsoniq.compiler.translator.JSONiqTranslator;
import io.zorba.api.InMemoryStore;
import io.zorba.api.SerializationOptions;
import io.zorba.api.Zorba;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ZorbaDGScriptEngine implements DGScriptEngine
{
    /**
     *
     */
    private ZorbaDGScriptContext             context;

    /**
     *
     */
    private final ZorbaDGScriptContext       defaultContext;

    /**
     *
     */
    private final ZorbaDGScriptEngineFactory factory;

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
    public ZorbaDGScriptEngine(ZorbaDGScriptEngineFactory factory)
    {
        this.factory = factory;
        session = null;
        defaultContext = new ZorbaDGScriptContext(this);
        translator = new JSONiqTranslator(JSONiqTranslator.Target.JSONIQ10);
    }

    /**
     * @param factory
     * @param session
     */
    public ZorbaDGScriptEngine(ZorbaDGScriptEngineFactory factory, DGSession session)
    {
        this.factory = factory;
        this.session = session;
        defaultContext = new ZorbaDGScriptContext(this, session);
        translator = new JSONiqTranslator(JSONiqTranslator.Target.JSONIQ10);
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
    public ZorbaDGCompiledScript compile(Reader script) throws ScriptException
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
    public ZorbaDGCompiledScript compile(String script) throws ScriptException
    {
        return new ZorbaDGCompiledScript(this, ZORBA.compileQuery(translate(script)));
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
        try (ZorbaDGCompiledScript compiledScript = new ZorbaDGCompiledScript(this,
                ZORBA.compileQuery(translate(script), ((ZorbaDGScriptContext) context).sctx))) {
            return compiledScript.eval();
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
    public ZorbaDGScriptContext getContext()
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
    public ZorbaDGScriptEngineFactory getFactory()
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
        this.context = (ZorbaDGScriptContext) context;
    }

    /**
     * @param script
     * @return
     */
    protected String translate(String script)
    {
        try {
            return translator.translate(script);
        } catch (final DGException e) {
            return script;
        }
    }

    /**
     *
     */
    static final SerializationOptions OPTIONS;
    /**
     *
     */
    static final Zorba                ZORBA;

    static {
        try {
            final String path = ZorbaDGScriptEngine.class.getProtectionDomain().getCodeSource().getLocation().toURI()
                    .getPath();
            if (path.endsWith("classes/")) {
                final File location = new File(path).getParentFile().getParentFile().getParentFile().getParentFile();
                if (SystemUtils.IS_OS_WINDOWS)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        loadNativeLibraries(new File(location, "target/windows-msvc-x86_64"));
                        break;
                    }
                else if (SystemUtils.IS_OS_LINUX)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        loadNativeLibraries(new File(location, "target/linux-gcc-x86_64"));
                        break;
                    }
            } else if (path.contains("WEB-INF")) {
                final File location = new File("C:\\Users\\Omar\\Documents\\GitHub\\unity");
                if (SystemUtils.IS_OS_WINDOWS)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        loadNativeLibraries(new File(location, "target/windows-msvc-x86_64"));
                        break;
                    }
                else if (SystemUtils.IS_OS_LINUX)
                    switch (SystemUtils.OS_ARCH) {
                    case "amd64":
                    case "x86_64":
                        loadNativeLibraries(new File(location, "target/linux-gcc-x86_64"));
                        break;
                    }
            } else {
                final File location = new File(path).getParentFile().getParentFile();
                loadNativeLibraries(location);
            }

            OPTIONS = new SerializationOptions();
            OPTIONS.setOmitXMLDeclaration(SerializationOptions.OmitXMLDeclaration.ZORBA_API_OMIT_XML_DECLARATION_YES);
            OPTIONS.setIndent(SerializationOptions.Indent.ZORBA_API_INDENT_YES);
            ZORBA = Zorba.getInstance(InMemoryStore.getInstance());
            Runtime.getRuntime().addShutdownHook(new Thread()
            {
                @Override
                public void run()
                {
                    ZORBA.shutdown();
                    InMemoryStore.shutdown(InMemoryStore.getInstance());
                }
            });
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param path
     */
    @SuppressWarnings("unchecked")
    private static void loadNativeLibraries(File path)
    {
        try {
            final Field loadedLibraryNamesField = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            loadedLibraryNamesField.setAccessible(true);
            final Set<String> loadedLibraryNames = new HashSet<>(
                    (Vector<String>) loadedLibraryNamesField.get(ZorbaDGScriptEngine.class.getClassLoader()));
            String[] libraryNames = new String[] {};
            if (SystemUtils.IS_OS_WINDOWS)
                switch (SystemUtils.OS_ARCH) {
                case "amd64":
                case "x86_64":
                    path = new File(path, "bin");
                    libraryNames = new String[] { "msvcr100.dll", "msvcp100.dll", "msvcr110.dll", "msvcp110.dll",
                            "libiconv.dll", "icudt48.dll", "icuuc48.dll", "icuin48.dll", "libxml2.dll",
                            "xerces-c_3_1.dll", "zorba_simplestore.dll", "zorba_api_java.dll",
                            "unity-module-engine-zorba.dll" };
                    break;
                }
            else if (SystemUtils.IS_OS_LINUX)
                switch (SystemUtils.OS_ARCH) {
                case "amd64":
                case "x86_64":
                    path = new File(path, "lib");
                    libraryNames = new String[] { "libzorba_simplestore.so.3.0.0", "libzorba_api_java.so",
                            "libunity-module-engine-zorba.so" };
                    break;
                }
            for (final String libraryName : libraryNames) {
                final String library = new File(path, libraryName).getAbsolutePath();
                if (!loadedLibraryNames.contains(library))
                    System.load(library);
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
