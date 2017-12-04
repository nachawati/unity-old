package unity.repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Base64;

import org.gitlab4j.api.models.RepositoryFile;

import unity.api.DXException;

public class UnityDXFileReader extends Reader
{
    private final Reader reader;

    public UnityDXFileReader(final UnityDXFile file) throws DXException
    {
        try {
            if (file.object instanceof RepositoryFile)
                reader = new StringReader(
                        new String(Base64.getDecoder().decode(((RepositoryFile) file.object).getContent())));
            else
                reader = new InputStreamReader(file.api().getRepositoryFileApi().getRawFile(file.getProject().getId(),
                        file.getCommit().getId(), file.getPath()));
        } catch (final Exception e) {
            throw new DXException(e);
        }
    }

    @Override
    public void close() throws IOException
    {
        reader.close();
    }

    @Override
    public int read() throws IOException
    {
        return reader.read();
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException
    {
        return reader.read(cbuf, off, len);
    }
}