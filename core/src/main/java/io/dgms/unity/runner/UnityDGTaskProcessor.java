/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.runner;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.api.DGScriptEngine;
import io.dgms.unity.system.UnityDGTask;
import io.dgms.unity.system.UnityDGTaskExecution;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGTaskProcessor implements Runnable
{
    /**
     *
     */
    private final UnityDGTaskRunner runner;

    /**
     * @param runner
     */
    public UnityDGTaskProcessor(UnityDGTaskRunner runner)
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
                final UnityDGTaskExecution execution = runner.poll();
                if (execution == null)
                    continue;
                else
                    try {
                        final UnityDGTask task = execution.getTask();
                        final UnityDGSession session = new UnityDGSession(task.getPrivateToken());
                        try (DGScriptEngine engine = session.getSystem().getEngineByName("zorba")) {
                            if (task.getPackageReference() != null)
                                engine.addPackage(task.getPackageReference());
                            final String result = engine.eval(task.getScript()).toString();
                            runner.finished(execution.getId(), result, null, null);
                        }
                    } catch (final Exception e) {
                        runner.failed(execution.getId(), null, null, e.getMessage());
                    }
            } catch (final Exception e) {
            } finally {
            }
    }
}