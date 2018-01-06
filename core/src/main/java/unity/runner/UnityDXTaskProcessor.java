/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.runner;

import unity.UnityDXSession;
import unity.api.DXScriptEngine;
import unity.system.UnityDXTask;
import unity.system.UnityDXTaskExecution;

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
                        final UnityDXTask task = execution.getTask();
                        final UnityDXSession session = new UnityDXSession(task.getPrivateToken());
                        try (DXScriptEngine engine = session.getSystem().getEngineByName("zorba")) {
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