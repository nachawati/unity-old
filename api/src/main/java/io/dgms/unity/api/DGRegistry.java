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

/**
 * @author Mohamad Omar Nachawati
 *
 */
public interface DGRegistry extends DGSessionObject
{
    /**
     * @param packageReference
     * @return DGPackage
     * @throws DGException
     */
    DGPackage getPackage(DGPackageReference packageReference) throws DGException;

    /**
     * @param packageReference
     * @param resolveDependencies
     * @return DGPackage
     * @throws DGException
     */
    DGPackage getPackage(DGPackageReference packageReference, boolean resolveDependencies) throws DGException;

    /**
     * @param name
     * @param version
     * @return DGPackageReference
     */
    DGPackageReference getPackageReference(String name, String version);

    /**
     * @param packageReference
     * @param path
     * @return InputStream
     * @throws DGException
     */
    InputStream getResourceAsStream(DGPackageReference packageReference, String path) throws DGException;

    /**
     * @param packageReference
     * @param namespace
     * @return InputStream
     * @throws DGException
     */
    InputStream getResourceAsStream(DGPackageReference packageReference, URI namespace) throws DGException;
}
