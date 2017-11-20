package io.dgms.unity.repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Base64;

import org.gitlab4j.api.models.RepositoryFile;

import io.dgms.unity.api.DGException;

public class UnityDGFileReader extends Reader
{
    private final Reader reader;

    public UnityDGFileReader(final UnityDGFile file) throws DGException
    {
        try {
            if (file.object instanceof RepositoryFile)
                reader = new StringReader(
                        new String(Base64.getDecoder().decode(((RepositoryFile) file.object).getContent())));
            else
                reader = new InputStreamReader(file.api().getRepositoryFileApi().getRawFile(file.getProject().getId(),
                        file.getCommit().getId(), file.getPath()));
        } catch (final Exception e) {
            throw new DGException(e);
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