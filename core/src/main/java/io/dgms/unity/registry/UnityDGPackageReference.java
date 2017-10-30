/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.registry;

import java.io.Serializable;

import javax.persistence.Embeddable;

import io.dgms.unity.api.DGPackageReference;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Embeddable
public class UnityDGPackageReference implements DGPackageReference, Serializable
{
    /**
     *
     */
    private String name;

    /**
     *
     */
    private String version;

    /**
     *
     */
    public UnityDGPackageReference()
    {
    }

    /**
     * @param name
     * @param version
     */
    public UnityDGPackageReference(String name, String version)
    {
        this.name = name;
        this.version = version;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final UnityDGPackageReference other = (UnityDGPackageReference) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGPackageReference#getName()
     */
    @Override
    public String getName()
    {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGPackageReference#getVersion()
     */
    @Override
    public String getVersion()
    {
        return version;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (version == null ? 0 : version.hashCode());
        return result;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @param version
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return name + "@" + version;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
