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
import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DXPackage extends DXSessionObject, DXPackageReference
{
    /**
     * @return Stream<? extends DXPackageReference>
     */
    Stream<? extends DXPackageReference> getDeclaredDependencies();

    /**
     * @return Stream<? extends DXPackageReference>
     */
    Stream<? extends DXPackageReference> getDependencies();

    /**
     * @param path
     * @return InputStream
     * @throws DXException
     */
    InputStream getResourceAsStream(String path) throws DXException;

    /**
     * @param namespace
     * @return InputStream
     * @throws DXException
     */
    InputStream getResourceAsStream(URI namespace) throws DXException;
}
