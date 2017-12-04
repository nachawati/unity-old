/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.client;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import unity.api.DXException;
import unity.api.DXPackageReference;
import unity.api.DXRegistry;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ClientDXRegistry extends ClientDXSessionObject implements DXRegistry
{
    /**
     * @param session
     */
    public ClientDXRegistry(ClientDXSession session)
    {
        super(session);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRegistry#getPackage(unity.api.DXPackageReference)
     */
    @Override
    public ClientDXPackage getPackage(DXPackageReference packageReference) throws DXException
    {
        return getPackage(packageReference, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRegistry#getPackage(unity.api.DXPackageReference, boolean)
     */
    @Override
    public ClientDXPackage getPackage(DXPackageReference packageReference, boolean resolveDependencies)
            throws DXException
    {
        try (Reader reader = new InputStreamReader(getResourceAsStream(packageReference, "package.json"))) {
            final JsonObject object = new Gson().fromJson(reader, JsonObject.class);
            return new ClientDXPackage(getSession(), this, object, resolveDependencies);
        } catch (final Exception e) {
            final JsonObject object = new JsonObject();
            object.addProperty("name", packageReference.getName());
            object.addProperty("version", packageReference.getVersion());
            return new ClientDXPackage(getSession(), this, object, false);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRegistry#getPackageReference(java.lang.String,
     * java.lang.String)
     */
    @Override
    public ClientDXPackageReference getPackageReference(String name, String version)
    {
        return new ClientDXPackageReference(name, version);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRegistry#getResourceAsStream(unity.api.DXPackageReference,
     * java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(DXPackageReference packageReference, String path) throws DXException
    {
        final ClientDXProject project = session.getSystem().getProject(packageReference.getName());
        final ClientDXCommit commit = project.getRepository().getCommit(packageReference.getVersion());
        return commit.getResourceAsStream(path);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXRegistry#getResourceAsStream(unity.api. DXPackageReference,
     * java.net.URI)
     */
    @Override
    public InputStream getResourceAsStream(DXPackageReference packageReference, URI namespace) throws DXException
    {
        final ClientDXProject project = session.getSystem().getProject(packageReference.getName());
        final ClientDXCommit commit = project.getRepository().getCommit(packageReference.getVersion());
        return commit.getResourceAsStream(namespace);
    }
}
