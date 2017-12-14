package unity.cli;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.script.ScriptException;

import org.junit.Test;

import unity.modules.engines.nashorn.NashornDXScriptEngine;
import unity.modules.engines.nashorn.NashornDXScriptEngineFactory;

public class SelectionTest
{
    @Test
    public void test() throws FileNotFoundException, IOException, ScriptException
    {
        try (final NashornDXScriptEngine engine = new NashornDXScriptEngineFactory().getScriptEngine()) {
            try (Reader reader = new FileReader("test.ts")) {
                System.out.println(engine.eval(reader));
            }
        }
    }
}
