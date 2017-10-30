/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.client;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import io.dgms.unity.api.DGTask;
import io.dgms.unity.api.DGTaskStatus;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Entity
public class ClientDGTask extends ClientDGSessionObject implements DGTask, Serializable
{
    /**
     *
     */
    private Instant                  dateSubmitted;

    /**
     *
     */
    @Id
    @GeneratedValue
    private Long                     id;

    /**
     *
     */
    private String                   name;

    /**
     *
     */
    private ClientDGPackageReference packageReference;

    /**
     *
     */
    private String                   privateToken;

    /**
     *
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private transient String         script;

    /**
     *
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private transient String         standardInput;

    /**
     *
     */
    @Enumerated(EnumType.STRING)
    private DGTaskStatus             status;

    /**
     *
     */
    private Integer                  userId;

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTask#getDateSubmitted()
     */
    @Override
    public Instant getDateSubmitted()
    {
        return dateSubmitted;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTask#getId()
     */
    @Override
    public Long getId()
    {
        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTask#getName()
     */
    @Override
    public String getName()
    {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTask#getPackageReference()
     */
    @Override
    public ClientDGPackageReference getPackageReference()
    {
        return packageReference;
    }

    /**
     * @return String
     */
    public String getPrivateToken()
    {
        return privateToken;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTask#getScript()
     */
    @Override
    public String getScript()
    {
        return script;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTask#getStandardInput()
     */
    @Override
    public String getStandardInput()
    {
        return standardInput;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTask#getStatus()
     */
    @Override
    public DGTaskStatus getStatus()
    {
        return status;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGTask#getUserId()
     */
    @Override
    public Integer getUserId()
    {
        return userId;
    }

    /**
     * @param dateSubmitted
     */
    public void setDateSubmitted(Instant dateSubmitted)
    {
        this.dateSubmitted = dateSubmitted;
    }

    /**
     * @param id
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @param packageReference
     */
    public void setPackageReference(ClientDGPackageReference packageReference)
    {
        this.packageReference = packageReference;
    }

    /**
     * @param privateToken
     */
    public void setPrivateToken(String privateToken)
    {
        this.privateToken = privateToken;
    }

    /**
     * @param script
     */
    public void setScript(String script)
    {
        this.script = script;
    }

    /**
     * @param standardInput
     */
    public void setStandardInput(String standardInput)
    {
        this.standardInput = standardInput;
    }

    /**
     * @param status
     */
    public void setStatus(DGTaskStatus status)
    {
        this.status = status;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}