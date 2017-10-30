/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.api;

import java.io.InputStream;
import java.net.URI;
import java.util.stream.Stream;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGPackage extends DGSessionObject, DGPackageReference
{
    /**
     * @return Stream<? extends DGPackageReference>
     */
    Stream<? extends DGPackageReference> getDeclaredDependencies();

    /**
     * @return Stream<? extends DGPackageReference>
     */
    Stream<? extends DGPackageReference> getDependencies();

    /**
     * @param path
     * @return InputStream
     * @throws DGException
     */
    InputStream getResourceAsStream(String path) throws DGException;

    /**
     * @param namespace
     * @return InputStream
     * @throws DGException
     */
    InputStream getResourceAsStream(URI namespace) throws DGException;
}
