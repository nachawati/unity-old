/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.net.URI;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGSession
{
    /**
     * @return URI
     */
    URI getGitLabHost();

    /**
     * @return URI
     */
    URI getHost();

    /**
     * @param name
     * @param version
     * @return DGPackageReference
     */
    DGPackageReference getPackageReference(String name, String version);

    /**
     * @return String
     */
    String getPrivateToken();

    /**
     * @return DGRegistry
     */
    DGRegistry getRegistry();

    /**
     * @return DGSystem
     */
    DGSystem getSystem();

    /**
     * @return DGUser
     */
    DGUser getUser();

    /**
     * @return String
     */
    String getVersion();
}
