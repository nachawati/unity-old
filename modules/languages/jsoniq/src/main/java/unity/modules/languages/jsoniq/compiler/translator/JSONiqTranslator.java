/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.languages.jsoniq.compiler.translator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import unity.api.DXException;
import unity.modules.languages.jsoniq.compiler.parser.JSONiq10Handler;
import unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser;
import unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser.ParseException;

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
     * @throws DXException
     * @throws IOException
     */
    public String translate(Reader reader) throws DXException, IOException
    {
        return translate(IOUtils.toString(reader));
    }

    /**
     * @param script
     * @return
     * @throws DXException
     */
    public String translate(String script) throws DXException
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
            throw new DXException(parser.getErrorMessage(e));
        } catch (final Exception e) {
            throw new DXException(e);
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
     * @throws SAXNotSupportedException
     * @throws SAXNotRecognizedException
     */
    private static Transformer loadTransformer(String path) throws IOException, TransformerFactoryConfigurationError,
            TransformerConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
    {
        try (InputStream stream = JSONiqTranslator.class.getResourceAsStream(path)) {
            final TransformerFactory factory = TransformerFactory.newInstance();
            factory.setErrorListener(new NullErrorListener());
            final Transformer transformer = factory.newTransformer(new StreamSource(stream));
            transformer.setErrorListener(new NullErrorListener());
            return transformer;
        }
    }

    private static class NullErrorListener implements ErrorListener
    {
        @Override
        public void error(TransformerException exception) throws TransformerException
        {
        }

        @Override
        public void fatalError(TransformerException exception) throws TransformerException
        {
        }

        @Override
        public void warning(TransformerException exception) throws TransformerException
        {
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
