/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.languages.jsoniq.compiler.parser;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class JSONiq10Handler implements JSONiq10Parser.EventHandler
{
    /**
     *
     */
    protected Node         currentNode;

    /**
     *
     */
    protected Document     document;

    /**
     *
     */
    protected CharSequence text;

    /**
     *
     */
    public JSONiq10Handler()
    {
        document = BUILDER.getDOMImplementation().createDocument(null, null, null);
        currentNode = document;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser.
     * EventHandler#endNonterminal(java.lang.String, int)
     */
    @Override
    public void endNonterminal(final String name, final int end)
    {
        final Attr attr = document.createAttribute("end");
        attr.setValue(Integer.toString(end));
        currentNode.getAttributes().setNamedItem(attr);
        currentNode = currentNode.getParentNode();
    }

    /**
     * @return
     */
    public Document getDocument()
    {
        return document;
    }

    /**
     *
     */
    public void minimize()
    {
        final TreeWalker walker = ((DocumentTraversal) document).createTreeWalker(document.getDocumentElement(),
                NodeFilter.SHOW_ELEMENT, null, true);
        traverseLevel(walker);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser.
     * EventHandler#reset(java.lang.CharSequence)
     */
    @Override
    public void reset(final CharSequence text)
    {
        this.text = text;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser.
     * EventHandler#startNonterminal(java.lang.String, int)
     */
    @Override
    public void startNonterminal(final String name, final int begin)
    {
        final Element element = document.createElement(name);
        element.setAttribute("begin", Integer.toString(begin));
        currentNode.appendChild(element);
        currentNode = element;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser.
     * EventHandler#terminal(java.lang.String, int, int)
     */
    @Override
    public void terminal(final String name, final int begin, final int end)
    {
        final Element element = document.createElement("terminal");
        element.setAttribute("name", name);
        element.setAttribute("begin", Integer.toString(begin));
        element.setAttribute("end", Integer.toString(end));
        element.setTextContent(text.subSequence(begin, end).toString());
        currentNode.appendChild(element);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        try {
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            final StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(getDocument()), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * @param walker
     */
    protected void traverseLevel(final TreeWalker walker)
    {
        final Node currentNode = walker.getCurrentNode();

        if (currentNode != document && currentNode.getParentNode() != document) {
            final String b1 = ((Element) currentNode).getAttribute("begin");
            final String e1 = ((Element) currentNode).getAttribute("end");
            final String b2 = ((Element) currentNode.getParentNode()).getAttribute("begin");
            final String e2 = ((Element) currentNode.getParentNode()).getAttribute("end");
            if (b1.equals(b2) && e1.equals(e2) && !currentNode.getNodeName().equals("terminal"))
                currentNode.getParentNode().getParentNode().replaceChild(currentNode, currentNode.getParentNode());
        }

        for (Node n = walker.firstChild(); n != null; n = walker.nextSibling())
            traverseLevel(walker);
        walker.setCurrentNode(currentNode);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.modules.languages.jsoniq.compiler.parser.JSONiq10Parser.
     * EventHandler#whitespace(int, int)
     */
    @Override
    public void whitespace(final int begin, final int end)
    {
        final Element element = document.createElement("terminal");
        element.setAttribute("type", "whitespace");
        element.setAttribute("begin", Integer.toString(begin));
        element.setAttribute("end", Integer.toString(end));
        element.setTextContent(text.subSequence(begin, end).toString());
        currentNode.appendChild(element);
    }

    /**
     *
     */
    protected static DocumentBuilder        BUILDER;
    /**
     *
     */
    protected static DocumentBuilderFactory FACTORY;

    static {
        try {
            FACTORY = DocumentBuilderFactory.newInstance();
            FACTORY.setNamespaceAware(true);
            BUILDER = FACTORY.newDocumentBuilder();
        } catch (final Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}