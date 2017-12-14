/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.runner;

<<<<<<< HEAD:core/src/main/java/io/dgms/unity/runner/UnityDGTaskProcessor.java
import io.dgms.unity.UnityDGSession;
import io.dgms.unity.api.DGScriptEngine;
import io.dgms.unity.modules.engines.zorba.ZorbaDGScriptEngineFactory;
import io.dgms.unity.system.UnityDGTask;
import io.dgms.unity.system.UnityDGTaskExecution;
=======
import unity.UnityDXSession;
import unity.api.DXScriptEngine;
import unity.system.UnityDXTask;
import unity.system.UnityDXTaskExecution;
>>>>>>> 5f276a37a85e21b845cc9ede283e805ba8685565:core/src/main/java/unity/runner/UnityDXTaskProcessor.java

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXTaskProcessor implements Runnable
{
    /**
     *
     */
    private final UnityDXTaskRunner runner;

    /**
     * @param runner
     */
    public UnityDXTaskProcessor(UnityDXTaskRunner runner)
    {
        this.runner = runner;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        while (runner.isRunning())
            try {
                final UnityDXTaskExecution execution = runner.poll();
                if (execution == null)
                    continue;
                else
                    try {
<<<<<<< HEAD:core/src/main/java/io/dgms/unity/runner/UnityDGTaskProcessor.java
                        final UnityDGTask task = execution.getTask();
                        final UnityDGSession session = new UnityDGSession(task.getPrivateToken());

                        try (DGScriptEngine engine = new ZorbaDGScriptEngineFactory().getScriptEngine(session)) {
                            // try (DGScriptEngine engine = session.getSystem().getEngineByName("zorba")) {
=======
                        final UnityDXTask task = execution.getTask();
                        final UnityDXSession session = new UnityDXSession(task.getPrivateToken());
                        try (DXScriptEngine engine = session.getSystem().getEngineByName("zorba")) {
>>>>>>> 5f276a37a85e21b845cc9ede283e805ba8685565:core/src/main/java/unity/runner/UnityDXTaskProcessor.java
                            if (task.getPackageReference() != null)
                                engine.addPackage(task.getPackageReference());
                            final String result = engine.eval(task.getScript()).toString();
                            runner.finished(execution.getId(), result, null, null);
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                        runner.failed(execution.getId(), null, null, e.getMessage());
                    }
            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
            }
    }
}