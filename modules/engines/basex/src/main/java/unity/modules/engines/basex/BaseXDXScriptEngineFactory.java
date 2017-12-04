/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.basex;

import java.util.Arrays;
import java.util.List;

import org.basex.BaseX;

import unity.api.DXScriptEngine;
import unity.api.DXScriptEngineFactory;
import unity.api.DXSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class BaseXDXScriptEngineFactory implements DXScriptEngineFactory
{
    /**
     *
     */
    public BaseXDXScriptEngineFactory()
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
        return "BaseX";
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.script.ScriptEngineFactory#getEngineVersion()
     */
    @Override
    public String getEngineVersion()
    {
        return BaseX.class.getPackage().getImplementationVersion();
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
        return "JSONiq (1.0), XQuery (1.0, 3.0, 3.1)";
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
     * @see io.dgms.unity.api.DXScriptEngineFactory#getScriptEngine()
     */
    @Override
    public BaseXDXScriptEngine getScriptEngine()
    {
        return new BaseXDXScriptEngine(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * io.dgms.unity.api.DXScriptEngineFactory#getScriptEngine(io.dgms.unity.api.
     * DXSession)
     */
    @Override
    public DXScriptEngine getScriptEngine(DXSession session)
    {
        return new BaseXDXScriptEngine(this, session);
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
    private static final List<String> names      = Arrays.asList(new String[] { "basex" });
}
