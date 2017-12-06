/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.zorba;

import java.util.Arrays;
import java.util.List;

import unity.api.DXScriptEngineFactory;
import unity.api.DXSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ZorbaDXScriptEngineFactory implements DXScriptEngineFactory
{
    /**
     *
     */
    public ZorbaDXScriptEngineFactory()
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
        return "Zorba";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getEngineVersion()
     */
    @Override
    public String getEngineVersion()
    {
        return "3.0";
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
        return "JSONiq, XQuery";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getLanguageVersion()
     */
    @Override
    public String getLanguageVersion()
    {
        return "JSONiq (1.0), XQuery (1.0, 3.0)";
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
    public ZorbaDXScriptEngine getScriptEngine()
    {
        return new ZorbaDXScriptEngine(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptEngineFactory#getScriptEngine(unity.api.DXSession)
     */
    @Override
    public ZorbaDXScriptEngine getScriptEngine(DXSession session)
    {
        return new ZorbaDXScriptEngine(this, session);
    }

    /**
     *
     */
    private static final List<String> extensions = Arrays
            .asList(new String[] { "jq", "jsoniq", "module", "xq", "xquery" });
    /**
     *
     */
    private static final List<String> mimeTypes  = Arrays.asList(new String[] { "text/xquery", "text/jsoniq" });
    /**
     *
     */
    private static final List<String> names      = Arrays.asList(new String[] { "zorba" });
}
