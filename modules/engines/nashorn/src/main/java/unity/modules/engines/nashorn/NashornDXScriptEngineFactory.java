/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.nashorn;

import java.util.Arrays;
import java.util.List;

import unity.api.DXScriptEngine;
import unity.api.DXScriptEngineFactory;
import unity.api.DXSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class NashornDXScriptEngineFactory implements DXScriptEngineFactory
{
    /**
     *
     */
    public NashornDXScriptEngineFactory()
    {
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getEngineName()
     */
    @Override
    public String getEngineName()
    {
        return "Nashorn";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getEngineVersion()
     */
    @Override
    public String getEngineVersion()
    {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getExtensions()
     */
    @Override
    public List<String> getExtensions()
    {
        return extensions;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getLanguageName()
     */
    @Override
    public String getLanguageName()
    {
        return "JavaScript, ECMAScript, TypeScript";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getLanguageVersion()
     */
    @Override
    public String getLanguageVersion()
    {
        return "JavaScript (5.1), ECMAScript (5.1), TypeScript (1.7)";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getMethodCallSyntax(java.lang.String,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public String getMethodCallSyntax(String obj, String m, String... args)
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getMimeTypes()
     */
    @Override
    public List<String> getMimeTypes()
    {
        return mimeTypes;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getNames()
     */
    @Override
    public List<String> getNames()
    {
        return names;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getOutputStatement(java.lang.String)
     */
    @Override
    public String getOutputStatement(String toDisplay)
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getParameter(java.lang.String)
     */
    @Override
    public Object getParameter(String key)
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getProgram(java.lang.String[])
     */
    @Override
    public String getProgram(String... statements)
    {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngineFactory#getScriptEngine()
     */
    @Override
    public NashornDXScriptEngine getScriptEngine()
    {
        return new NashornDXScriptEngine(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngineFactory#getScriptEngine(unity.api. DXSession)
     */
    @Override
    public DXScriptEngine getScriptEngine(DXSession session)
    {
        return new NashornDXScriptEngine(this, session);
    }

    /**
     *
     */
    private static final List<String> extensions = Arrays.asList(new String[] { "js", "ts" });
    /**
     *
     */
    private static final List<String> mimeTypes  = Arrays
            .asList(new String[] { "application/javascript", "application/emcascript", "application/typescript",
                    "text/javascript", "text/emcascript", "text/typescript" });
    /**
     *
     */
    private static final List<String> names      = Arrays.asList(new String[] { "nashorn" });
}
