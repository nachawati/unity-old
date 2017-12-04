/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.runner;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import unity.api.DXException;
import unity.api.DXTaskExecutionStatus;
import unity.api.DXTaskStatus;
import unity.persistence.UnityDXEntityManager;
import unity.registry.UnityDXPackageReference;
import unity.system.UnityDXTask;
import unity.system.UnityDXTaskExecution;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXTaskRunner
{
    /**
     *
     */
    private final EntityManagerFactory emf;

    /**
     *
     */
    private final ExecutorService      executor;

    /**
     *
     */
    private final AtomicBoolean        running = new AtomicBoolean(true);

    /**
     * @param emf
     */
    public UnityDXTaskRunner(EntityManagerFactory emf)
    {
        this.emf = emf;
        try (UnityDXEntityManager em = new UnityDXEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final Query query = em.createQuery(
                    "UPDATE UnityDXTaskExecution e SET e.status = :interrupted, e.dateTerminated = :dateTerminated WHERE e.status = :active");
            query.setParameter("interrupted", DXTaskExecutionStatus.INTERRUPTED);
            query.setParameter("dateTerminated", Instant.now());
            query.setParameter("active", DXTaskExecutionStatus.ACTIVE);
            query.executeUpdate();
            em.getTransaction().commit();
        }
        executor = Executors.newWorkStealingPool();
        executor.execute(new UnityDXTaskProcessor(this));
        executor.execute(new UnityDXTaskProcessor(this));
    }

    /**
     * @param taskExecutionId
     * @param result
     * @param standardOutput
     * @param standardError
     * @throws DXException
     */
    public synchronized void failed(Long taskExecutionId, String result, String standardOutput, String standardError)
            throws DXException
    {
        try (UnityDXEntityManager em = new UnityDXEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final UnityDXTaskExecution execution = em.find(UnityDXTaskExecution.class, taskExecutionId);
            execution.setDateTerminated(Instant.now());
            execution.setResult(result);
            execution.setStandardError(standardError);
            execution.setStandardOutput(standardOutput);
            execution.setStatus(DXTaskExecutionStatus.FAILED);
            execution.getTask().setStatus(DXTaskStatus.FAILED);
            em.getTransaction().commit();
        } catch (final Exception e) {
            throw new DXException(e);
        }
    }

    /**
     * @param taskExecutionId
     * @param result
     * @param standardOutput
     * @param standardError
     * @throws DXException
     */
    public synchronized void finished(Long taskExecutionId, String result, String standardOutput, String standardError)
            throws DXException
    {
        try (UnityDXEntityManager em = new UnityDXEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final UnityDXTaskExecution execution = em.find(UnityDXTaskExecution.class, taskExecutionId);
            execution.setDateTerminated(Instant.now());
            execution.setResult(result);
            execution.setStandardError(standardError);
            execution.setStandardOutput(standardOutput);
            execution.setStatus(DXTaskExecutionStatus.FINISHED);
            execution.getTask().setStatus(DXTaskStatus.FINISHED);
            em.getTransaction().commit();
        } catch (final Exception e) {
            throw new DXException(e);
        }
    }

    /**
     * @return
     */
    public boolean isRunning()
    {
        return running.get();
    }

    /**
     * @return
     */
    public synchronized UnityDXTaskExecution poll()
    {
        try (UnityDXEntityManager em = new UnityDXEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final Query query = em.createQuery("SELECT e FROM UnityDXTaskExecution e WHERE e.status = :queued");
            query.setParameter("queued", DXTaskExecutionStatus.QUEUED);
            final UnityDXTaskExecution execution = (UnityDXTaskExecution) query.getSingleResult();
            execution.setDateInitiated(Instant.now());
            execution.setStatus(DXTaskExecutionStatus.ACTIVE);
            em.getTransaction().commit();
            return execution;
        } catch (final Exception e) {
        }
        try {
            wait(60000);
        } catch (final InterruptedException e) {
        }
        return null;
    }

    /**
     *
     */
    public void shutdown()
    {
        running.set(false);
        executor.shutdownNow();
    }

    /**
     * @param userId
     * @param privateToken
     * @param name
     * @param script
     * @param packageReference
     * @return
     * @throws DXException
     */
    public synchronized UnityDXTaskExecution submit(Integer userId, String privateToken, String name, String script,
            UnityDXPackageReference packageReference) throws DXException
    {
        try (UnityDXEntityManager em = new UnityDXEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final UnityDXTask task = new UnityDXTask();
            task.setDateSubmitted(Instant.now());
            task.setName(name);
            task.setPackageReference(packageReference);
            task.setPrivateToken(privateToken);
            task.setScript(script);
            task.setStatus(DXTaskStatus.PENDING);
            task.setUserId(userId);
            em.persist(task);

            final UnityDXTaskExecution execution = new UnityDXTaskExecution();
            execution.setStatus(DXTaskExecutionStatus.QUEUED);
            execution.setTask(task);
            em.persist(execution);

            em.getTransaction().commit();

            notify();

            return execution;
        } catch (final Exception e) {
            throw new DXException(e);
        }
    }
}
