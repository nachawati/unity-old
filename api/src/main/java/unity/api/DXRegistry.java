/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.api;

import java.io.InputStream;
import java.net.URI;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXRegistry extends DXSessionObject
{
    /**
     * @param packageReference
     * @return DXPackage
     * @throws DXException
     */
    DXPackage getPackage(DXPackageReference packageReference) throws DXException;

    /**
     * @param packageReference
     * @param resolveDependencies
     * @return DXPackage
     * @throws DXException
     */
    DXPackage getPackage(DXPackageReference packageReference, boolean resolveDependencies) throws DXException;

    /**
     * @param name
     * @param version
     * @return DXPackageReference
     */
    DXPackageReference getPackageReference(String name, String version);

    /**
     * @param packageReference
     * @param path
     * @return InputStream
     * @throws DXException
     */
    InputStream getResourceAsStream(DXPackageReference packageReference, String path) throws DXException;

    /**
     * @param packageReference
     * @param namespace
     * @return InputStream
     * @throws DXException
     */
    InputStream getResourceAsStream(DXPackageReference packageReference, URI namespace) throws DXException;
}
