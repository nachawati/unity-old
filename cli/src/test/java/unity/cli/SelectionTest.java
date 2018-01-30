package unity.cli;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.script.ScriptException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;
import org.xml.sax.SAXException;

import unity.modules.engines.zorba.ZorbaDXScriptEngine;
import unity.modules.engines.zorba.ZorbaDXScriptEngineFactory;

public class SelectionTest
{
    @Test
    public void test() throws FileNotFoundException, IOException, ScriptException, ParserConfigurationException, SAXException
    {
    	/*
        try (final NashornDXScriptEngine engine = new NashornDXScriptEngineFactory().getScriptEngine()) {
            try (Reader reader = new FileReader("test.ts")) {
                System.out.println(engine.eval(reader));
            }
        }*/
    	
    	try (final ZorbaDXScriptEngine engine = new ZorbaDXScriptEngineFactory().getScriptEngine()) {
            try (Reader reader = new FileReader("test.jq")) {
                System.out.println(engine.eval(reader));
            }
        }
    }
}
