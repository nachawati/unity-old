/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.languages.ampl;

import com.ampl.AMPL;
import com.ampl.Environment;
import com.ampl.Variable;

import io.zorba.api.Item;
import io.zorba.api.ItemSequence;
import io.zorba.api.Iterator;
import io.zorba.api.StringPair;
import io.zorba.api.StringPairVector;
import io.zorba.api.Zorba;
import unity.api.DXScriptContext;
import unity.api.DXSystem;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ZorbaModule
{
    /**
     * @param argument
     * @return
     */
    private static String getStringArgument(ItemSequence argument)
    {
        final Item item = Item.createEmptyItem();
        final Iterator i = argument.getIterator();
        i.open();
        i.next(item);
        i.close();
        return item.getStringValue();
    }

    /**
     * @param zorba
     * @param context
     * @param model
     * @param solver
     * @return
     */
    public static ItemSequence solve(Zorba zorba, DXScriptContext context, ItemSequence model, ItemSequence solver)
    {
        final Environment env = new Environment(DXSystem.getInstallPath().resolve("bin").toAbsolutePath().toString());
        final StringPairVector solution = new StringPairVector();
        try (AMPL ampl = new AMPL(env)) {
            /*
             * ampl.setErrorHandler(new ErrorHandler() {
             *
             * @Override public void error(AMPLException e) { }
             *
             * @Override public void warning(AMPLException e) { } });
             * ampl.setOutputHandler(o -> { });
             */
            ampl.setOption("solver", getStringArgument(solver));
            final String modelString = unescape(getStringArgument(model));
            ampl.eval(modelString);
            ampl.solve();
            for (final Variable v : ampl.getVariables())
                solution.add(new StringPair(v.get().name(), Double.toString(v.get().value())));
            solution.add(new StringPair("objective", Double.toString(ampl.getObjective("obj").value())));
            solution.add(new StringPair("result", ampl.getObjective("obj").result()));
            return new ItemSequence(zorba.getItemFactory().createJSONObject(solution));
        } catch (final Throwable t) {
            solution.add(new StringPair("error", t.getMessage()));
            return new ItemSequence(zorba.getItemFactory().createJSONObject(solution));
        }
    }

    /**
     * @param text
     * @return
     */
    private static String unescape(String text)
    {
        final StringBuilder result = new StringBuilder(text.length());
        int i = 0;
        final int n = text.length();
        while (i < n) {
            final char charAt = text.charAt(i);
            if (charAt != '&') {
                result.append(charAt);
                i++;
            } else if (text.startsWith("&amp;", i)) {
                result.append('&');
                i += 5;
            } else if (text.startsWith("&apos;", i)) {
                result.append('\'');
                i += 6;
            } else if (text.startsWith("&quot;", i)) {
                result.append('"');
                i += 6;
            } else if (text.startsWith("&lt;", i)) {
                result.append('<');
                i += 4;
            } else if (text.startsWith("&gt;", i)) {
                result.append('>');
                i += 4;
            } else
                i++;
        }
        return result.toString();
    }

}
