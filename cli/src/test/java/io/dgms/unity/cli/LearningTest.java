package io.dgms.unity.cli;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.script.ScriptException;

import org.junit.Test;

import io.dgms.unity.modules.engines.basex.BaseXDGScriptEngine;
import io.dgms.unity.modules.engines.basex.BaseXDGScriptEngineFactory;

public class LearningTest
{
    @Test
    public void test() throws IOException, ScriptException
    {
        try (final BaseXDGScriptEngine engine = new BaseXDGScriptEngineFactory().getScriptEngine()) {
            try (Reader reader = new FileReader("test.jq")) {
                System.out.println(engine.eval(reader));
            }
        }
    }
}
