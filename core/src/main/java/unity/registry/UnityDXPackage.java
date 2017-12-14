/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.registry;

import java.io.InputStream;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXException;
import unity.api.DXPackage;
import unity.api.DXPackageReference;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDXPackage extends UnityDXSessionObject implements DXPackage
{
    /**
     *
     */
    private final LinkedHashMap<String, DXPackageReference> dependencies = new LinkedHashMap<>();

    /**
     *
     */
    private final String                                    name;

    /**
     *
     */
    private final JsonObject                                object;

    /**
     *
     */
    private final UnityDXRegistry                           registry;

    /**
     *
     */
    private final String                                    version;

    /**
     * @param session
     * @param registry
     * @param object
     * @param resolveDependencies
     */
    public UnityDXPackage(UnityDXSession session, UnityDXRegistry registry, JsonObject object,
            boolean resolveDependencies)
    {
        super(session);
        this.registry = registry;
        this.object = object;
        name = object.get("name").getAsString();
        version = object.get("version").getAsString();
        if (resolveDependencies)
            resolveDependencies(object);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXPackage#getDeclaredDependencies()
     */
    @Override
    public Stream<DXPackageReference> getDeclaredDependencies()
    {
        return object.getAsJsonObject("dependencies").entrySet().stream()
                .map(d -> new UnityDXPackageReference(d.getKey(), d.getValue().getAsString()));
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXPackage#getDependencies()
     */
    @Override
    public Stream<DXPackageReference> getDependencies()
    {
        return dependencies.values().stream();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXPackageReference#getName()
     */
    @Override
    public String getName()
    {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXPackage#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String path) throws DXException
    {
        try {
            return registry.getResourceAsStream(this, path);
        } catch (final DXException e) {
        }

        for (final DXPackageReference dependency : dependencies.values())
            try {
                return registry.getResourceAsStream(dependency, path);
            } catch (final DXException e) {
            }

        throw new DXException();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXPackage#getResourceAsStream(java.net.URI)
     */
    @Override
    public InputStream getResourceAsStream(URI namespace) throws DXException
    {
        try {
            return registry.getResourceAsStream(this, namespace);
        } catch (final DXException e) {
        }

        for (final DXPackageReference dependency : dependencies.values())
            try {
                return registry.getResourceAsStream(dependency, namespace);
            } catch (final DXException e) {
            }

        throw new DXException();
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXPackageReference#getVersion()
     */
    @Override
    public String getVersion()
    {
        return version;
    }

    /**
     * @param object
     */
    private void resolveDependencies(JsonObject object)
    {
        object.getAsJsonObject("dependencies").entrySet().stream()
                .map(d -> new UnityDXPackageReference(d.getKey(), d.getValue().getAsString())).forEach(d -> {
                    if (dependencies.containsKey(d.getName()))
                        return;
                    try {
                        final UnityDXPackage dependency = registry.getPackage(d, false);
                        dependencies.put(d.getName(), dependency);
                        resolveDependencies(dependency.object);
                    } catch (final DXException e) {
                    }
                });
    }
}
