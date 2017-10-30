/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.languages.jsoniq.compiler.translator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import io.dgms.unity.api.DGException;
import io.dgms.unity.modules.languages.jsoniq.compiler.parser.JSONiq10Handler;
import io.dgms.unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser;
import io.dgms.unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser.ParseException;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class JSONiqTranslator
{
    /**
     *
     */
    private final Target target;

    /**
     * @param target
     */
    public JSONiqTranslator(Target target)
    {
        if (target == null)
            throw new NullPointerException();
        this.target = target;
    }

    /**
     * @return
     */
    public Target getTarget()
    {
        return target;
    }

    /**
     * @param reader
     * @return
     * @throws DGException
     * @throws IOException
     */
    public String translate(Reader reader) throws DGException, IOException
    {
        return translate(IOUtils.toString(reader));
    }

    /**
     * @param script
     * @return
     * @throws DGException
     */
    public String translate(String script) throws DGException
    {
        JSONiq10Parser parser = null;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document;
            final JSONiq10Handler handler = new JSONiq10Handler();
            parser = new JSONiq10Parser(script, handler);
            parser.parse_XQuery();
            handler.minimize();
            document = handler.getDocument();
            switch (target) {
            case XQUERY31:
                XQUERY31.transform(new DOMSource(document), new StreamResult(output));
                break;
            case JSONIQ10:
            default:
                JSONIQ10.transform(new DOMSource(document), new StreamResult(output));
            }
            return output.toString("UTF-8");
        } catch (final ParseException e) {
            throw new DGException(parser.getErrorMessage(e));
        } catch (final Exception e) {
            throw new DGException(e);
        }
    }

    /**
     *
     */
    private final static Transformer JSONIQ10;
    /**
     *
     */
    private final static Transformer XQUERY31;

    static {
        try {
            JSONIQ10 = loadTransformer("/xslt/jsoniq-10.xslt");
            XQUERY31 = loadTransformer("/xslt/xquery-31.xslt");
        } catch (final Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * @param path
     * @return
     * @throws IOException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     */
    private static Transformer loadTransformer(String path)
            throws IOException, TransformerFactoryConfigurationError, TransformerConfigurationException
    {
        try (InputStream stream = JSONiqTranslator.class.getResourceAsStream(path)) {
            return TransformerFactory.newInstance().newTransformer(new StreamSource(stream));
        }
    }

    /**
     * @author Mohamad Omar Nachawati
     *
     */
    public static enum Source
    {
        JSONIQ10
    }

    /**
     * @author Mohamad Omar Nachawati
     *
     */
    public static enum Target
    {
        JSONIQ10, XQUERY31
    }
}
