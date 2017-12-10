package unity.cli;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.script.ScriptException;

import org.junit.Test;

import unity.modules.engines.basex.BaseXDXScriptEngine;
import unity.modules.engines.basex.BaseXDXScriptEngineFactory;

public class SelectionTest
{
    @Test
    public void test() throws FileNotFoundException, IOException, ScriptException
    {
        try (final BaseXDXScriptEngine engine = new BaseXDXScriptEngineFactory().getScriptEngine()) {
            try (Reader reader = new FileReader("test.jq")) {
                System.out.println(engine.eval(reader));
            }
        }
    }
}
