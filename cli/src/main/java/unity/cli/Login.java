/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

package unity.cli;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import unity.UnityDXSession;
import unity.api.DXException;
import unity.api.DXSession;
import unity.client.ClientDXSession;

/**
 * @author Mohamad Omar Nachawati
 *
 */
@Parameters(commandNames = "login", commandDescription = "Log into Unity")
public class Login extends Command
{
    /**
     *
     */
    @Parameter(names = {
            "--gitLabHost" }, description = "<url> : GitLab Host URL (Local Execution)", required = false, order = 2)
    protected String       gitLabHost;

    /**
     *
     */
    @Parameter(names = {
            "--host" }, description = "<url> : Unity Host URL (Remote Execution)", required = false, order = 1)
    protected String       host = "https://dgms.io/";

    /**
     *
     */
    @Parameter(description = "<username>", required = true, order = 0)
    protected List<String> username;

    /*
     * (non-Javadoc)
     *
     * @see unity.cli.Command#action()
     */
    @Override
    public void action() throws DXException
    {
        System.out.print(ansi().fgBright(CYAN).a("Password: ").reset());
        System.out.flush();
        final String password = new String(System.console().readPassword());

        final DXSession session;
        if (gitLabHost != null)
            session = new UnityDXSession(URI.create(gitLabHost), username.get(0), password);
        else
            session = new ClientDXSession(URI.create(host), username.get(0), password);

        save(session);

        System.out.println("Welcome to Unity");
        if (session.getUser().getLastSignInDate() != null) {
            final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
            final ZonedDateTime lastLogin = ZonedDateTime.ofInstant(session.getUser().getLastSignInDate(),
                    ZoneId.systemDefault());
            System.out.println("Last login: " + ansi().fgBright(GREEN).a(formatter.format(lastLogin)).reset());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see unity.cli.Command#action(unity.api.DXSession)
     */
    @Override
    public void action(DXSession session) throws DXException
    {
        action();
    }

    /**
     * @param session
     * @throws DXException
     */
    private void save(DXSession session) throws DXException
    {
        final Gson gson = new Gson();
        try (JsonWriter writer = gson
                .newJsonWriter(Files.newBufferedWriter(Main.getSystemPath().resolve("session.json")))) {
            final JsonObject sessionProperties = new JsonObject();
            if (session instanceof UnityDXSession)
                sessionProperties.addProperty("gitLabHost", gitLabHost);
            else
                sessionProperties.addProperty("host", host);
            sessionProperties.addProperty("privateToken", session.getPrivateToken());
            gson.toJson(sessionProperties, writer);
        } catch (final IOException e) {
            throw new DXException("unable to save session");
        }
    }
}
