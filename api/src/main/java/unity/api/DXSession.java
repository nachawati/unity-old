/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.net.URI;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXSession
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
     * @return DXPackageReference
     */
    DXPackageReference getPackageReference(String name, String version);

    /**
     * @return String
     */
    String getPrivateToken();

    /**
     * @return DXRegistry
     */
    DXRegistry getRegistry();

    /**
     * @return DXSystem
     */
    DXSystem getSystem();

    /**
     * @return DXUser
     */
    DXUser getUser();

    /**
     * @return String
     */
    String getVersion();
}
