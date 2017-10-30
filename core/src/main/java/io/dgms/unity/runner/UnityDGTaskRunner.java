/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.runner;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGTaskExecutionStatus;
import io.dgms.unity.api.DGTaskStatus;
import io.dgms.unity.persistence.UnityDGEntityManager;
import io.dgms.unity.registry.UnityDGPackageReference;
import io.dgms.unity.system.UnityDGTask;
import io.dgms.unity.system.UnityDGTaskExecution;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGTaskRunner
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
    public UnityDGTaskRunner(EntityManagerFactory emf)
    {
        this.emf = emf;
        try (UnityDGEntityManager em = new UnityDGEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final Query query = em.createQuery(
                    "UPDATE UnityDGTaskExecution e SET e.status = :interrupted, e.dateTerminated = :dateTerminated WHERE e.status = :active");
            query.setParameter("interrupted", DGTaskExecutionStatus.INTERRUPTED);
            query.setParameter("dateTerminated", Instant.now());
            query.setParameter("active", DGTaskExecutionStatus.ACTIVE);
            query.executeUpdate();
            em.getTransaction().commit();
        }
        executor = Executors.newWorkStealingPool();
        executor.execute(new UnityDGTaskProcessor(this));
        executor.execute(new UnityDGTaskProcessor(this));
    }

    /**
     * @param taskExecutionId
     * @param result
     * @param standardOutput
     * @param standardError
     * @throws DGException
     */
    public synchronized void failed(Long taskExecutionId, String result, String standardOutput, String standardError)
            throws DGException
    {
        try (UnityDGEntityManager em = new UnityDGEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final UnityDGTaskExecution execution = em.find(UnityDGTaskExecution.class, taskExecutionId);
            execution.setDateTerminated(Instant.now());
            execution.setResult(result);
            execution.setStandardError(standardError);
            execution.setStandardOutput(standardOutput);
            execution.setStatus(DGTaskExecutionStatus.FAILED);
            execution.getTask().setStatus(DGTaskStatus.FAILED);
            em.getTransaction().commit();
        } catch (final Exception e) {
            throw new DGException(e);
        }
    }

    /**
     * @param taskExecutionId
     * @param result
     * @param standardOutput
     * @param standardError
     * @throws DGException
     */
    public synchronized void finished(Long taskExecutionId, String result, String standardOutput, String standardError)
            throws DGException
    {
        try (UnityDGEntityManager em = new UnityDGEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final UnityDGTaskExecution execution = em.find(UnityDGTaskExecution.class, taskExecutionId);
            execution.setDateTerminated(Instant.now());
            execution.setResult(result);
            execution.setStandardError(standardError);
            execution.setStandardOutput(standardOutput);
            execution.setStatus(DGTaskExecutionStatus.FINISHED);
            execution.getTask().setStatus(DGTaskStatus.FINISHED);
            em.getTransaction().commit();
        } catch (final Exception e) {
            throw new DGException(e);
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
    public synchronized UnityDGTaskExecution poll()
    {
        try (UnityDGEntityManager em = new UnityDGEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final Query query = em.createQuery("SELECT e FROM UnityDGTaskExecution e WHERE e.status = :queued");
            query.setParameter("queued", DGTaskExecutionStatus.QUEUED);
            final UnityDGTaskExecution execution = (UnityDGTaskExecution) query.getSingleResult();
            execution.setDateInitiated(Instant.now());
            execution.setStatus(DGTaskExecutionStatus.ACTIVE);
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
     * @throws DGException
     */
    public synchronized UnityDGTaskExecution submit(Integer userId, String privateToken, String name, String script,
            UnityDGPackageReference packageReference) throws DGException
    {
        try (UnityDGEntityManager em = new UnityDGEntityManager(emf.createEntityManager())) {
            em.getTransaction().begin();
            final UnityDGTask task = new UnityDGTask();
            task.setDateSubmitted(Instant.now());
            task.setName(name);
            task.setPackageReference(packageReference);
            task.setPrivateToken(privateToken);
            task.setScript(script);
            task.setStatus(DGTaskStatus.PENDING);
            task.setUserId(userId);
            em.persist(task);

            final UnityDGTaskExecution execution = new UnityDGTaskExecution();
            execution.setStatus(DGTaskExecutionStatus.QUEUED);
            execution.setTask(task);
            em.persist(execution);

            em.getTransaction().commit();

            notify();

            return execution;
        } catch (final Exception e) {
            throw new DGException(e);
        }
    }
}
