/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.modules.engines.nashorn;

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
public class NashornDXScriptContext extends SimpleScriptContext implements DXScriptContext, UriResolver
{
    /**
     *
     */
    final Context                       context;

    /**
     *
     */
    private final NashornDXScriptEngine engine;

    /**
     *
     */
    private final String[]              extensions = new String[] { ".jqm", ".xqm", ".module", ".jq", ".xq", ".jqy",
            ".xqy", ".jql", ".xql", ".xqu", ".jsoniq", ".xquery" };

    /**
     *
     */
    private final Set<DXPackage>        packages   = new LinkedHashSet<>();

    /**
     *
     */
    private final Set<Path>             paths      = new LinkedHashSet<>();

    /**
     *
     */
    private final DXSession             session;

    /**
     * @param engine
     */
    public NashornDXScriptContext(NashornDXScriptEngine engine)
    {
        this(engine, null);
    }

    /**
     * @param engine
     * @param session
     */
    public NashornDXScriptContext(NashornDXScriptEngine engine, DXSession session)
    {
        this.engine = engine;
        this.session = session;
        context = new Context();
        try {
            paths.addAll(Files.list(DXSystem.getInstallPath().resolve("modules")).collect(Collectors.toList()));
        } catch (final IOException e) {
        }
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
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.api.DXScriptContext#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(final String path) throws IOException
    {
        System.out.println(path);
        for (final Path p : paths) {
            final Path fullPath = p.resolve(path);
            if (Files.isRegularFile(fullPath))
                return Files.newInputStream(fullPath);

            if (Files.isDirectory(fullPath)) {
                for (final String extension : extensions) {
                    if (Files.exists(fullPath.resolve("index" + extension + ".basex")))
                        return Files.newInputStream(fullPath.resolve("index" + extension + ".basex"));
                    if (Files.exists(fullPath.resolve("index" + extension)))
                        return Files.newInputStream(fullPath.resolve("index" + extension));
                }
                continue;
            }
            for (final String extension : extensions) {
                if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + extension + ".basex")))
                    return Files.newInputStream(
                            fullPath.getParent().resolve(fullPath.getFileName() + extension + ".basex"));
                if (Files.isRegularFile(fullPath.getParent().resolve(fullPath.getFileName() + extension)))
                    return Files.newInputStream(fullPath.getParent().resolve(fullPath.getFileName() + extension));
            }
        }

        for (final DXPackage pkg : packages) {
            try {
                return pkg.getResourceAsStream(path);
            } catch (final DXException e) {
            }
            for (final String extension : extensions) {
                try {
                    return pkg.getResourceAsStream(path + extension + ".basex");
                } catch (final DXException e) {
                }
                try {
                    return pkg.getResourceAsStream(path + extension);
                } catch (final DXException e) {
                }
            }
            for (final String extension : extensions) {
                try {
                    return pkg.getResourceAsStream(path + "/index" + extension + ".basex");
                } catch (final DXException e) {
                }
                try {
                    return pkg.getResourceAsStream(path + "/index" + extension);
                } catch (final DXException e) {
                }
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
            path = DXSystem.toPathString(uri);
            return new IOContent(engine.translate(getResourceAsString(path)).getBytes(), path);
        } catch (final Exception e) {
            return null;
        }
    }
}
