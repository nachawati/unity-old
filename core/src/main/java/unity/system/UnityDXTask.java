/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.system;

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

import unity.UnityDXSessionObject;
import unity.api.DXPackageReference;
import unity.api.DXTask;
import unity.api.DXTaskStatus;
import unity.registry.UnityDXPackageReference;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Entity
public class UnityDXTask extends UnityDXSessionObject implements DXTask, Serializable
{
    /**
     *
     */
    private Instant                 dateSubmitted;

    /**
     *
     */
    @Id
    @GeneratedValue
    private Long                    id;

    /**
     *
     */
    private UnityDXPackageReference packageReference;

    /**
     *
     */
    private String                  privateToken;

    /**
     *
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String                  script;

    /**
     *
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String                  standardInput;

    /**
     *
     */
    @Enumerated(EnumType.STRING)
    private DXTaskStatus            status;

    /**
     *
     */
    private String                  taskName;

    /**
     *
     */
    private Integer                 userId;

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTask#getDateSubmitted()
     */
    @Override
    public Instant getDateSubmitted()
    {
        return dateSubmitted;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTask#getId()
     */
    @Override
    public Long getId()
    {
        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTask#getName()
     */
    @Override
    public String getName()
    {
        return taskName;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTask#getPackageReference()
     */
    @Override
    public DXPackageReference getPackageReference()
    {
        return packageReference;
    }

    /**
     * @return
     */
    public String getPrivateToken()
    {
        return privateToken;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTask#getScript()
     */
    @Override
    public String getScript()
    {
        return script;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTask#getStandardInput()
     */
    @Override
    public String getStandardInput()
    {
        return standardInput;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTask#getStatus()
     */
    @Override
    public DXTaskStatus getStatus()
    {
        return status;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXTask#getUserId()
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
        taskName = name;
    }

    /**
     * @param packageReference
     */
    public void setPackageReference(UnityDXPackageReference packageReference)
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
    public void setStatus(DXTaskStatus status)
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
