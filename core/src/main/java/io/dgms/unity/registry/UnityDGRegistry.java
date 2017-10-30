/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.registry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGRegistry;
import io.dgms.unity.repository.UnityDGCommit;
import io.dgms.unity.system.UnityDGProject;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGRegistry extends UnityDGSessionObject implements DGRegistry
{
    /**
     * @param session
     */
    public UnityDGRegistry(UnityDGSession session)
    {
        super(session);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * io.dgms.unity.api.DGRegistry#getPackage(io.dgms.unity.api.DGPackageReference)
     */
    @Override
    public UnityDGPackage getPackage(DGPackageReference packageReference) throws DGException
    {
        return getPackage(packageReference, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * io.dgms.unity.api.DGRegistry#getPackage(io.dgms.unity.api.DGPackageReference,
     * boolean)
     */
    @Override
    public UnityDGPackage getPackage(DGPackageReference packageReference, boolean resolveDependencies)
            throws DGException
    {
        try (Reader reader = new InputStreamReader(getResourceAsStream(packageReference, "package.json"))) {
            final JsonObject object = new Gson().fromJson(reader, JsonObject.class);
            return new UnityDGPackage(getSession(), this, object, resolveDependencies);
        } catch (final Exception e) {
            final JsonObject object = new JsonObject();
            object.addProperty("name", packageReference.getName());
            object.addProperty("version", packageReference.getVersion());
            return new UnityDGPackage(getSession(), this, object, false);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRegistry#getPackageReference(java.lang.String,
     * java.lang.String)
     */
    @Override
    public UnityDGPackageReference getPackageReference(String name, String version)
    {
        return new UnityDGPackageReference(name, version);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRegistry#getResourceAsStream(io.dgms.unity.api.
     * DGPackageReference, java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(DGPackageReference packageReference, String path) throws DGException
    {
        final UnityDGProject project = session.getSystem().getProject(packageReference.getName());
        final UnityDGCommit commit = project.getRepository().getCommit(packageReference.getVersion());
        return commit.getResourceAsStream(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGRegistry#getResourceAsStream(io.dgms.unity.api.
     * DGPackageReference, java.net.URI)
     */
    @Override
    public InputStream getResourceAsStream(DGPackageReference packageReference, URI namespace) throws DGException
    {
        final UnityDGProject project = session.getSystem().getProject(packageReference.getName());
        final UnityDGCommit commit = project.getRepository().getCommit(packageReference.getVersion());
        return commit.getResourceAsStream(namespace);
    }
}
