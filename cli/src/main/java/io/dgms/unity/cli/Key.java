/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package io.dgms.unity.cli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.GREEN;

import java.io.ByteArrayOutputStream;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;

import io.dgms.unity.api.DGException;
import io.dgms.unity.api.DGSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "key", commandDescription = "Generate new SSH key")
public class Key extends Command
{
    /**
     *
     */
    @Parameter(names = { "-o", "--overwrite" }, description = "Overwrite existing key")
    protected boolean overwrite = false;
    /**
     *
     */
    @Parameter(names = { "-s", "--size" }, description = "Key size")
    protected int     size      = 4096;

    /*
     * (non-Javadoc)
     *
     * @see io.dgms.unity.cli.Command#action(io.dgms.unity.api.DGSession)
     */
    @Override
    public void action(DGSession session) throws Exception
    {
        System.out.print("Attempting to generate new SSH key... ");
        System.out.flush();
        final JSch jsch = new JSch();
        KeyPair keyPair = Main.loadKeyPair(session, session.getHost(), jsch);
        if (keyPair != null && !overwrite)
            throw new DGException("key already exists, overwrite option not set (-o, --overwrite)");
        keyPair = KeyPair.genKeyPair(jsch, KeyPair.RSA, size);
        Main.saveKeyPair(session, session.getGitLabHost(), keyPair);
        final ByteArrayOutputStream publicKey = new ByteArrayOutputStream();
        keyPair.writePublicKey(publicKey, "");
        session.getUser().addSshKey("Unity CLI", publicKey.toString("UTF-8"));
        System.out.println(ansi().fgBright(GREEN).a("DONE").reset());
    }
}