/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.zorba;

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

import io.zorba.api.DynamicContext;
import io.zorba.api.Item;
import io.zorba.api.ItemFactory;
import io.zorba.api.ItemSequence;
import io.zorba.api.Iterator;
import io.zorba.api.StaticContext;
import io.zorba.api.StringVector;
import io.zorba.api.Zorba;
import unity.api.DXException;
import unity.api.DXPackage;
import unity.api.DXPackageReference;
import unity.api.DXScriptContext;
import unity.api.DXSession;
import unity.api.DXSystem;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class ZorbaDXScriptContext extends SimpleScriptContext implements DXScriptContext
{
    /**
     *
     */
    private final Map<String, String>   cache           = Collections.synchronizedMap(new HashMap<>());

    /**
     *
     */
    private final ZorbaDXScriptEngine   engine;

    /**
     *
     */
    private final Map<String, Class<?>> externalModules = new HashMap<>();

    /**
     *
     */
    private final Set<DXPackage>        packages        = new LinkedHashSet<>();

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
    private final DXSession             session;

    /**
     * @param engine
     */
    public ZorbaDXScriptContext(ZorbaDXScriptEngine engine)
    {
        this(engine, null);
    }

    /**
     * @param engine
     * @param session
     */
    public ZorbaDXScriptContext(ZorbaDXScriptEngine engine, DXSession session)
    {
        this.engine = engine;
        this.session = session;
        sctx = ZorbaDXScriptEngine.ZORBA.createStaticContext();
        final StringVector uriPath = new StringVector();
        uriPath.add(DXSystem.getInstallPath().resolve("share/zorba/uris/core/3.0.0").toString());
        uriPath.add(DXSystem.getInstallPath().resolve("share/zorba/uris/core/2.9.0").toString());
        uriPath.add(DXSystem.getInstallPath().resolve("share/zorba/uris").toString());
        sctx.setURIPath(uriPath);
        final StringVector libPath = new StringVector();
        libPath.add(DXSystem.getInstallPath().resolve("lib/zorba/core/3.0.0").toString());
        libPath.add(DXSystem.getInstallPath().resolve("lib/zorba/core/2.9.0").toString());
        libPath.add(DXSystem.getInstallPath().resolve("lib/zorba").toString());
        sctx.setLIBPath(libPath);
        construct();
        registerExternalModule("http://dgms.io/unity/modules/languages/ampl",
                unity.modules.languages.ampl.ZorbaModule.class);
        try {
            paths.addAll(Files.list(DXSystem.getInstallPath().resolve("modules")).collect(Collectors.toList()));
        } catch (final IOException e) {
        	e.printStackTrace();
        }
        
        System.out.println(paths);
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptContext#addPackage(unity.api. DXPackageReference)
     */
    @Override
    public void addPackage(DXPackageReference packageReference) throws DXException
    {
        if (!packages.contains(packageReference))
            packages.add(session.getRegistry().getPackage(packageReference));
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptContext#addPackage(java.nio.file.Path)
     */
    @Override
    public void addPackage(Path path) throws DXException
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
     * @see unity.api.DXScriptContext#close()
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
            parameterTypes[1] = DXScriptContext.class;
            for (int i = 0; i < arguments.length; i++)
                parameterTypes[i + 2] = ItemSequence.class;
            final ItemFactory factory = ZorbaDXScriptEngine.ZORBA.getItemFactory();
            Object result;
            final Method method = module.getMethod(localName, parameterTypes);
            method.setAccessible(true);
            final Object[] parameters = new Object[arguments.length + 2];
            parameters[0] = ZorbaDXScriptEngine.ZORBA;
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
     * @see unity.api.DXScriptContext#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(final String path) throws IOException
    {
        for (final Path p : paths) {
            final Path fullPath = p.resolve(path);
            if (Files.isRegularFile(fullPath))
                return Files.newInputStream(fullPath);
            if (Files.isDirectory(fullPath)) {
                if (Files.exists(fullPath.resolve("index.jqm.zorba")))
                    return Files.newInputStream(fullPath.resolve("index.jqm.zorba"));
                if (Files.exists(fullPath.resolve("index.jqm")))
                    return Files.newInputStream(fullPath.resolve("index.jqm"));
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
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".jqm.zorba")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".jqm.zorba"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".jqm")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".jqm"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".xq.zorba")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".xq.zorba"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".xq")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".xq"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".module.zorba")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".module.zorba"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".module")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".module"));
        }

        for (final DXPackage pkg : packages) {
            try {
                return pkg.getResourceAsStream(path);
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".jqm.zorba");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".jqm");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".xq.zorba");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".xq");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".module.zorba");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".module");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.jqm.zorba");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.jqm");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.xq.zorba");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.xq");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.module.zorba");
            } catch (final DXException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.module");
            } catch (final DXException e) {
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
            return "file:///" + DXSystem.toPathString(identifier);
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
