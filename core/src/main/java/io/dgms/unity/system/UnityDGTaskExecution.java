/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.system;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGTaskExecution;
import io.dgms.unity.api.DGTaskExecutionStatus;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Entity
public class UnityDGTaskExecution extends UnityDGSessionObject implements DGTaskExecution, Serializable
{
    /**
     *
     */
    private Instant               dateInitiated;

    /**
     *
     */
    private Instant               dateTerminated;

    /**
     *
     */
    @Id
    @GeneratedValue
    private Long                  id;

    /**
     *
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String                result;

    /**
     *
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String                standardError;

    /**
     *
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String                standardOutput;

    /**
     *
     */
    @Enumerated(EnumType.STRING)
    private DGTaskExecutionStatus status;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private UnityDGTask           task;

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getDateInitiated()
     */
    @Override
    public Instant getDateInitiated()
    {
        return dateInitiated;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getDateTerminated()
     */
    @Override
    public Instant getDateTerminated()
    {
        return dateTerminated;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getExecutionDuration()
     */
    @Override
    public Duration getExecutionDuration()
    {
        if (dateInitiated == null)
            return Duration.ZERO;
        if (dateTerminated == null)
            return Duration.between(dateInitiated, Instant.now());
        return Duration.between(dateInitiated, dateTerminated);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getId()
     */
    @Override
    public Long getId()
    {
        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getResult()
     */
    @Override
    public String getResult()
    {
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getStandardError()
     */
    @Override
    public String getStandardError()
    {
        return standardError;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getStandardOutput()
     */
    @Override
    public String getStandardOutput()
    {
        return standardOutput;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getStatus()
     */
    @Override
    public DGTaskExecutionStatus getStatus()
    {
        return status;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTaskExecution#getTask()
     */
    @Override
    public UnityDGTask getTask()
    {
        return task;
    }

    /**
     * @param dateInitiated
     */
    public void setDateInitiated(Instant dateInitiated)
    {
        this.dateInitiated = dateInitiated;
    }

    /**
     * @param dateTerminated
     */
    public void setDateTerminated(Instant dateTerminated)
    {
        this.dateTerminated = dateTerminated;
    }

    /**
     * @param id
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @param result
     */
    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * @param standardError
     */
    public void setStandardError(String standardError)
    {
        this.standardError = standardError;
    }

    /**
     * @param standardOutput
     */
    public void setStandardOutput(String standardOutput)
    {
        this.standardOutput = standardOutput;
    }

    /**
     * @param status
     */
    public void setStatus(DGTaskExecutionStatus status)
    {
        this.status = status;
    }

    /**
     * @param task
     */
    public void setTask(UnityDGTask task)
    {
        this.task = task;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
