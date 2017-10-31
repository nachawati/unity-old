/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.engines.zorba;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.SimpleScriptContext;

import org.apache.commons.io.IOUtils;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackage;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGScriptContext;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.api.DGSystem;
import io.zorba.api.DynamicContext;
import io.zorba.api.Item;
import io.zorba.api.ItemFactory;
import io.zorba.api.ItemSequence;
import io.zorba.api.Iterator;
import io.zorba.api.StaticContext;
import io.zorba.api.StringVector;
import io.zorba.api.Zorba;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ZorbaDGScriptContext extends SimpleScriptContext implements DGScriptContext
{
    /**
     *
     */
    private final Map<String, String>   cache           = Collections.synchronizedMap(new HashMap<>());

    /**
     *
     */
    private final ZorbaDGScriptEngine   engine;

    /**
     *
     */
    private final Map<String, Class<?>> externalModules = new HashMap<>();

    /**
     *
     */
    private final Set<DGPackage>        packages        = new LinkedHashSet<>();

    /**
     *
     */
    private final Set<Path>             paths           = new LinkedHashSet<>();

    /**
     *
     */
    private final long                  resolverCPtr    = 0;

    /**
     *
     */
    final StaticContext                 sctx;

    /**
     *
     */
    private final DGSession             session;

    /**
     * @param engine
     */
    public ZorbaDGScriptContext(ZorbaDGScriptEngine engine)
    {
        this(engine, null);
    }

    /**
     * @param engine
     * @param session
     */
    public ZorbaDGScriptContext(ZorbaDGScriptEngine engine, DGSession session)
    {
        this.engine = engine;
        this.session = session;
        sctx = ZorbaDGScriptEngine.ZORBA.createStaticContext();
        final StringVector uriPath = new StringVector();
        uriPath.add(DGSystem.getInstallPath().resolve("share/zorba/uris/core/3.0.0").toString());
        uriPath.add(DGSystem.getInstallPath().resolve("share/zorba/uris/core/2.9.0").toString());
        uriPath.add(DGSystem.getInstallPath().resolve("share/zorba/uris").toString());
        sctx.setURIPath(uriPath);
        final StringVector libPath = new StringVector();
        libPath.add(DGSystem.getInstallPath().resolve("lib/zorba/core/3.0.0").toString());
        libPath.add(DGSystem.getInstallPath().resolve("lib/zorba/core/2.9.0").toString());
        libPath.add(DGSystem.getInstallPath().resolve("lib/zorba").toString());
        sctx.setLIBPath(libPath);
        construct();
        registerExternalModule("http://dgms.io/unity/modules/languages/ampl",
                io.dgms.unity.modules.languages.ampl.ZorbaModule.class);
        try {
            paths.addAll(Files.list(DGSystem.getInstallPath().resolve("modules")).collect(Collectors.toList()));
        } catch (final IOException e) {
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptContext#addPackage(io.dgms.unity.api.
     * DGPackageReference)
     */
    @Override
    public void addPackage(DGPackageReference packageReference) throws DGException
    {
        if (!packages.contains(packageReference))
            packages.add(session.getRegistry().getPackage(packageReference));
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptContext#addPackage(java.nio.file.Path)
     */
    @Override
    public void addPackage(Path path) throws DGException
    {
        try {
            paths.add(path);
            paths.addAll(Files.list(path.resolve("dgms_modules")).collect(Collectors.toList()));
        } catch (final IOException e) {
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptContext#close()
     */
    @Override
    public void close()
    {
        sctx.destroy();
        destroy();
    }

    /**
     * @return
     */
    private native boolean construct();

    /**
     * @return
     */
    private native boolean destroy();

    /**
     * @param moduleURI
     * @param localName
     * @param sctx
     * @param dctx
     * @param arguments
     * @return
     */
    private Object evaluate(final String moduleURI, final String localName, final StaticContext sctx,
            final DynamicContext dctx, final ItemSequence... arguments)
    {
        final Class<?> module = externalModules.get(moduleURI);
        if (module == null)
            return null;
        try {
            final Class<?>[] parameterTypes = new Class<?>[arguments.length + 2];
            parameterTypes[0] = Zorba.class;
            parameterTypes[1] = DGScriptContext.class;
            for (int i = 0; i < arguments.length; i++)
                parameterTypes[i + 2] = ItemSequence.class;
            final ItemFactory factory = ZorbaDGScriptEngine.ZORBA.getItemFactory();
            Object result;
            final Method method = module.getMethod(localName, parameterTypes);
            method.setAccessible(true);
            final Object[] parameters = new Object[arguments.length + 2];
            parameters[0] = ZorbaDGScriptEngine.ZORBA;
            parameters[1] = this;
            for (int i = 0; i < arguments.length; i++)
                parameters[i + 2] = arguments[i];
            result = method.invoke(null, parameters);
            if (result == null)
                return new ItemSequence(factory.createJSONNull());
            if (result instanceof ItemSequence)
                return result;
            if (result instanceof Item)
                return new ItemSequence((Item) result);
            if (result instanceof Boolean)
                return new ItemSequence(factory.createBoolean((Boolean) result));
            if (result instanceof Byte)
                return new ItemSequence(factory.createByte((char) ((Byte) result).intValue()));
            if (result instanceof BigDecimal)
                return new ItemSequence(factory.createDecimal(((BigDecimal) result).toString()));
            if (result instanceof Double)
                return new ItemSequence(factory.createDouble((Double) result));
            if (result instanceof Float)
                return new ItemSequence(factory.createFloat((Float) result));
            if (result instanceof Integer)
                return new ItemSequence(factory.createInt((Integer) result));
            if (result instanceof Long)
                return new ItemSequence(factory.createLong((Long) result));
            if (result instanceof Short)
                return new ItemSequence(factory.createShort((Short) result));
            return new ItemSequence(factory.createString(result.toString()));
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.api.DGScriptContext#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(final String path) throws IOException
    {
        for (final Path p : paths) {
            final Path fullPath = p.resolve(path);
            if (Files.isRegularFile(fullPath))
                return Files.newInputStream(fullPath);
            if (Files.isDirectory(fullPath)) {
                if (Files.exists(fullPath.resolve("index.jq.zorba")))
                    return Files.newInputStream(fullPath.resolve("index.jq.zorba"));
                if (Files.exists(fullPath.resolve("index.jq")))
                    return Files.newInputStream(fullPath.resolve("index.jq"));
                if (Files.exists(fullPath.resolve("index.xq.zorba")))
                    return Files.newInputStream(fullPath.resolve("index.xq.zorba"));
                if (Files.exists(fullPath.resolve("index.xq")))
                    return Files.newInputStream(fullPath.resolve("index.xq"));
                if (Files.exists(fullPath.resolve("index.module.zorba")))
                    return Files.newInputStream(fullPath.resolve("index.module.zorba"));
                if (Files.exists(fullPath.resolve("index.module")))
                    return Files.newInputStream(fullPath.resolve("index.module"));
                continue;
            }
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".jq.zorba")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".jq.zorba"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".jq")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".jq"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".xq.zorba")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".xq.zorba"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".xq")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".xq"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".module.zorba")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".module.zorba"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".module")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".module"));
        }

        for (final DGPackage pkg : packages) {
            try {
                return pkg.getResourceAsStream(path);
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".jq.zorba");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".jq");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".xq.zorba");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".xq");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".module.zorba");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".module");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.jq.zorba");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.jq");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.xq.zorba");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.xq");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.module.zorba");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.module");
            } catch (final DGException e) {
            }
        }

        throw new FileNotFoundException();
    }

    /**
     * @param path
     * @return
     */
    private String getResourceAsString(final String path)
    {
        if (cache.containsKey(path))
            return cache.get(path);
        try (InputStream stream = getResourceAsStream(path)) {
            final String content = IOUtils.toString(stream, StandardCharsets.UTF_8);
            cache.put(path, content);
            return content;
        } catch (final IOException e) {
            return null;
        }
    }

    /**
     * @param identifier
     * @param kind
     * @return
     */
    private String map(final String identifier, final int kind)
    {
        try {
            if (identifier == null)
                return null;
            if (identifier.startsWith("http://jsoniq.org"))
                return null;
            if (identifier.startsWith("http://jsound.io"))
                return null;
            if (identifier.startsWith("http://www.functx.com"))
                return null;
            if (identifier.startsWith("http://www.w3.org"))
                return null;
            if (identifier.startsWith("http://expath.org"))
                return null;
            if (identifier.startsWith("http://www.zorba-xquery.com"))
                return null;
            if (identifier.startsWith("http://zorba.io"))
                return null;
            return "file:///" + DGSystem.toPathString(identifier);
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * @param namespace
     * @return
     */
    private native boolean register(String namespace);

    /**
     * @param namespace
     * @param clazz
     */
    public void registerExternalModule(String namespace, final Class<?> clazz)
    {
        try {
            if (!externalModules.containsKey(namespace))
                register(namespace);
            externalModules.put(namespace, clazz);
        } catch (final Exception e) {
        }
    }

    /**
     * @param identifier
     * @param kind
     * @return
     */
    private String resolve(String identifier, final int kind)
    {
        try {
            if (identifier == null)
                return null;
            if (new File(new URI(identifier)).exists())
                return null;
            switch (kind) {
            case 1:
                if (identifier.startsWith("file:///home/osboxes/Desktop/zorba-3.0/"))
                    return null;
                if (identifier.startsWith("file:///org/jsoniq/"))
                    return null;
                if (identifier.startsWith("file:///io/jsound/"))
                    return null;
                if (identifier.startsWith("file:///com/functx/www/"))
                    return null;
                if (identifier.startsWith("file:///org/w3/www/"))
                    return null;
                if (identifier.startsWith("file:///org/expath/"))
                    return null;
                if (identifier.startsWith("file:///com/zorba-xquery/www/"))
                    return null;
                if (identifier.startsWith("file:///io/zorba/"))
                    return null;
                if (identifier.startsWith("file:///"))
                    identifier = identifier.substring(8);
                if (identifier.endsWith(".module"))
                    identifier = identifier.substring(0, identifier.length() - 7);
                final String script = getResourceAsString(identifier);
                if (script == null)
                    return null;

                // System.out.println("-------------------------");
                // System.out.println("-------------------------");
                // System.out.println("START " + identifier);
                // System.out.println("-------------------------");
                // System.out.println("-------------------------");
                // System.out.println(engine.translate(script));
                // System.out.println("-------------------------");
                // System.out.println("-------------------------");
                // System.out.println("END " + identifier);
                // System.out.println("-------------------------");
                // System.out.println("-------------------------");

                return engine.translate(script);
            case 6:
                if (identifier.startsWith("file:///"))
                    identifier = identifier.substring(8);
                return IOUtils.toString(getResourceAsStream(identifier), StandardCharsets.UTF_8);
            default:
                return null;
            }
        } catch (final Exception e) {
            return null;
        }
    }

    /**
     * @param version
     * @return
     */
    private native final boolean setJSONiqVersion(final int version);

    /**
     * @param version
     * @return
     */
    private native final boolean setXQueryVersion(final int version);

    /**
     * @param argument
     * @return
     */
    private static Integer getInteger(ItemSequence argument)
    {
        final Item item = Item.createEmptyItem();
        final Iterator i = argument.getIterator();
        i.open();
        i.next(item);
        i.close();
        return item.getIntValue();
    }
}
