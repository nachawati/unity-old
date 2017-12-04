package unity.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.gitlab4j.api.models.RepositoryFile;

import unity.api.DXException;

public class UnityDXFileInputStream extends InputStream
{
    private final InputStream stream;

    public UnityDXFileInputStream(final UnityDXFile file) throws DXException
    {
        try {
            if (file.object instanceof RepositoryFile)
                stream = new ByteArrayInputStream(
                        Base64.getDecoder().decode(((RepositoryFile) file.object).getContent()));
            else
                stream = file.api().getRepositoryFileApi().getRawFile(file.getProject().getId(),
                        file.getCommit().getId(), file.getPath());
        } catch (final Exception e) {
            throw new DXException(e);
        }
    }

    @Override
    public void close() throws IOException
    {
        stream.close();
    }

    @Override
    public int read() throws IOException
    {
        return stream.read();
    }

    @Override
    public int read(final byte[] b) throws IOException
    {
        return stream.read(b);
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException
    {
        return stream.read(b, off, len);
    }
}
