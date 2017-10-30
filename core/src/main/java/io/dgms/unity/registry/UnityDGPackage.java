/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.registry;

import java.io.InputStream;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.stream.Stream;

import com.google.gson.JsonObject;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackage;
import io.dgms.unity.api.DGPackageReference;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class UnityDGPackage extends UnityDGSessionObject implements DGPackage
{
    /**
     *
     */
    private final LinkedHashMap<String, DGPackageReference> dependencies = new LinkedHashMap<>();

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
    private final UnityDGRegistry                           registry;

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
    public UnityDGPackage(UnityDGSession session, UnityDGRegistry registry, JsonObject object,
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
     * @see io.dgms.unity.api.DGPackage#getDeclaredDependencies()
     */
    @Override
    public Stream<DGPackageReference> getDeclaredDependencies()
    {
        return object.getAsJsonObject("dependencies").entrySet().stream()
                .map(d -> new UnityDGPackageReference(d.getKey(), d.getValue().getAsString()));
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGPackage#getDependencies()
     */
    @Override
    public Stream<DGPackageReference> getDependencies()
    {
        return dependencies.values().stream();
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
     * @see io.dgms.unity.api.DGPackage#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String path) throws DGException
    {
        try {
            return registry.getResourceAsStream(this, path);
        } catch (final DGException e) {
        }

        for (final DGPackageReference dependency : dependencies.values())
            try {
                return registry.getResourceAsStream(dependency, path);
            } catch (final DGException e) {
            }

        throw new DGException();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGPackage#getResourceAsStream(java.net.URI)
     */
    @Override
    public InputStream getResourceAsStream(URI namespace) throws DGException
    {
        try {
            return registry.getResourceAsStream(this, namespace);
        } catch (final DGException e) {
        }

        for (final DGPackageReference dependency : dependencies.values())
            try {
                return registry.getResourceAsStream(dependency, namespace);
            } catch (final DGException e) {
            }

        throw new DGException();
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

    /**
     * @param object
     */
    private void resolveDependencies(JsonObject object)
    {
        object.getAsJsonObject("dependencies").entrySet().stream()
                .map(d -> new UnityDGPackageReference(d.getKey(), d.getValue().getAsString())).forEach(d -> {
                    if (dependencies.containsKey(d.getName()))
                        return;
                    try {
                        final UnityDGPackage dependency = registry.getPackage(d, false);
                        dependencies.put(d.getName(), dependency);
                        resolveDependencies(dependency.object);
                    } catch (final DGException e) {
                    }
                });
    }
}
