/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.modules.engines.basex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.SimpleScriptContext;

import org.apache.commons.io.IOUtils;
import org.basex.core.Context;
import org.basex.io.IO;
import org.basex.io.IOContent;
import org.basex.query.util.UriResolver;
import org.basex.query.value.item.Uri;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGPackage;
import io.dgms.unity.api.DGPackageReference;
import io.dgms.unity.api.DGScriptContext;
import io.dgms.unity.api.DGSession;
import io.dgms.unity.api.DGSystem;

/**
 * @author Mohamad Omar Nachawati
 *
 */
public class BaseXDGScriptContext extends SimpleScriptContext implements DGScriptContext, UriResolver
{
    /**
     *
     */
    final Context                     context;

    /**
     *
     */
    private final BaseXDGScriptEngine engine;

    /**
     *
     */
    private final Set<DGPackage>      packages = new LinkedHashSet<>();

    /**
     *
     */
    private final Set<Path>           paths    = new LinkedHashSet<>();

    /**
     *
     */
    private final DGSession           session;

    /**
     * @param engine
     */
    public BaseXDGScriptContext(BaseXDGScriptEngine engine)
    {
        this(engine, null);
    }

    /**
     * @param engine
     * @param session
     */
    public BaseXDGScriptContext(BaseXDGScriptEngine engine, DGSession session)
    {
        this.engine = engine;
        this.session = session;
        context = new Context();
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
                if (Files.exists(fullPath.resolve("index.jq.basex")))
                    return Files.newInputStream(fullPath.resolve("index.jq.basex"));
                if (Files.exists(fullPath.resolve("index.jq")))
                    return Files.newInputStream(fullPath.resolve("index.jq"));
                if (Files.exists(fullPath.resolve("index.xq.basex")))
                    return Files.newInputStream(fullPath.resolve("index.xq.basex"));
                if (Files.exists(fullPath.resolve("index.xq")))
                    return Files.newInputStream(fullPath.resolve("index.xq"));
                if (Files.exists(fullPath.resolve("index.module.basex")))
                    return Files.newInputStream(fullPath.resolve("index.module.basex"));
                if (Files.exists(fullPath.resolve("index.module")))
                    return Files.newInputStream(fullPath.resolve("index.module"));
                continue;
            }
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".jq.basex")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".jq.basex"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".jq")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".jq"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".xq.basex")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".xq.basex"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".xq")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".xq"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".module.basex")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".module.basex"));
            if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + ".module")))
                return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + ".module"));
        }

        for (final DGPackage pkg : packages) {
            try {
                return pkg.getResourceAsStream(path);
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".jq.basex");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".jq");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".xq.basex");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".xq");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".module.basex");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + ".module");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.jq.basex");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.jq");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.xq.basex");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.xq");
            } catch (final DGException e) {
            }
            try {
                return pkg.getResourceAsStream(path + "/index.module.basex");
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
        try (InputStream stream = getResourceAsStream(path)) {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.basex.query.util.UriResolver#resolve(java.lang.String,
     * java.lang.String, org.basex.query.value.item.Uri)
     */
    @Override
    public IO resolve(String path, String uri, Uri base)
    {
        try {
            path = DGSystem.toPathString(uri);
            return new IOContent(engine.translate(getResourceAsString(path)).getBytes(), path);
        } catch (final Exception e) {
            return null;
        }
    }
}
