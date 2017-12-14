package unity.kernel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import unity.UnityDXSession;
import unity.UnityDXSessionObject;
import unity.api.DXScriptEngine;
import unity.system.UnityDXSystem;

public class UnityDXKernel extends UnityDXSessionObject implements AutoCloseable
{
    private final ZMQ.Context        context;
    private final ControlChannel     controlChannel;
    private final HeartBeatChannel   heartBeatChannel;
    private final String             id;
    private final InputChannel       inputChannel;
    private final OutputChannel      outputChannel;
    private final AtomicBoolean      running;
    private final ConnectionSettings settings;
    private final ShellChannel       shellChannel;

    public UnityDXKernel(Path connectionFile) throws IOException
    {
        this(null, connectionFile);
    }

    public UnityDXKernel(UnityDXSession session, Path connectionFile) throws IOException
    {
        super(session);
        try (Reader reader = Files.newBufferedReader(connectionFile)) {
            id = UUID.randomUUID().toString();
            settings = new Gson().fromJson(reader, ConnectionSettings.class);
            context = ZMQ.context(1);
            running = new AtomicBoolean(true);

            outputChannel = new OutputChannel();
            heartBeatChannel = new HeartBeatChannel();
            controlChannel = new ControlChannel();
            shellChannel = new ShellChannel();
            inputChannel = null;// new InputChannel();

            // outputChannel.start();
            shellChannel.start();
            controlChannel.start();
            heartBeatChannel.start();

            // inputChannel.start();
        }
    }

    @Override
    public void close()
    {
        running.set(false);
        heartBeatChannel.interrupt();
        shellChannel.interrupt();
        outputChannel.interrupt();
        controlChannel.interrupt();
        inputChannel.interrupt();
    }

    static DateFormat DF;

    static {
        DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
        DF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    protected abstract class Channel extends Thread
    {
        final byte[] key;
        final Socket socket;

        Channel(String address, int port, int type)
        {
            key = settings.key.getBytes(StandardCharsets.UTF_8);
            socket = context.socket(type);
            socket.bind("tcp://" + address + ":" + port);
        }

        void onMessage(Message message) throws Exception
        {
        }

        Message receive(ZMQ.Socket socket, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException
        {
            final Gson gson = new Gson();
            final ZMsg zMsg = ZMsg.recvMsg(socket);
            try {
                final Message message = new Message();
                message.identities = new LinkedList<>();
                final ZFrame[] zFrames = zMsg.toArray(new ZFrame[zMsg.size()]);
                boolean found = false;
                int current = 0;
                for (; current < zFrames.length; ++current) {
                    final String delim = new String(zFrames[current].getData(), StandardCharsets.UTF_8);
                    if ("<IDS|MSG>".equals(delim)) {
                        found = true;
                        break;
                    } else
                        message.identities.add(delim);
                }

                assert found;
                byte[] hmac = zFrames[++current].getData();
                hmac = DatatypeConverter.parseHexBinary(new String(hmac));

                final byte[] headerBytes = zFrames[++current].getData();
                final String header = new String(headerBytes, StandardCharsets.UTF_8);
                message.header = gson.fromJson(header, Header.class);

                final byte[] parentHeaderBytes = zFrames[++current].getData();
                final String parentHeader = new String(parentHeaderBytes, StandardCharsets.UTF_8);
                message.parentHeader = gson.fromJson(parentHeader, Header.class);

                final byte[] metadataBytes = zFrames[++current].getData();
                final String metadata = new String(metadataBytes, StandardCharsets.UTF_8);
                message.metadata = gson.fromJson(metadata, new TypeToken<Map<String, Object>>()
                {
                }.getType());

                final byte[] contentBytes = zFrames[++current].getData();
                final String content = new String(contentBytes, StandardCharsets.UTF_8);
                message.content = gson.fromJson(content, new TypeToken<Map<String, Object>>()
                {
                }.getType());

                final byte[][] data = { headerBytes, parentHeaderBytes, metadataBytes, contentBytes };
                final SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
                final Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(keySpec);
                for (int i = 0; i < 4; i++)
                    mac.update(data[i]);
                final byte[] digest = DatatypeConverter.printHexBinary(mac.doFinal()).toLowerCase().getBytes();

                // assert (Utils.digestEqual(digest, hmac));

                return message;
            } finally {
                zMsg.destroy();
            }
        }

        @Override
        public void run()
        {
            while (true)
                try {
                    onMessage(receive(socket, key));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
        }

        boolean send(Message message) throws InvalidKeyException, NoSuchAlgorithmException
        {
            final Gson gson = new Gson();
            final ZMsg zMsg = new ZMsg();
            if (message.identities != null)
                for (final String identity : message.identities)
                    zMsg.add(identity.getBytes(StandardCharsets.UTF_8));
            zMsg.add("<IDS|MSG>");

            byte[] header = null;
            if (message.header != null)
                header = gson.toJson(message.header).getBytes(StandardCharsets.UTF_8);
            else
                header = gson.toJson(new JsonObject()).getBytes(StandardCharsets.UTF_8);

            byte[] parentHeader = null;
            if (message.parentHeader != null)
                parentHeader = gson.toJson(message.parentHeader).getBytes(StandardCharsets.UTF_8);
            else
                parentHeader = gson.toJson(new JsonObject()).getBytes(StandardCharsets.UTF_8);

            byte[] metadata = null;
            if (message.metadata != null)
                metadata = gson.toJson(message.metadata).getBytes(StandardCharsets.UTF_8);
            else
                metadata = gson.toJson(new JsonObject()).getBytes(StandardCharsets.UTF_8);

            byte[] content = null;
            if (message.content != null)
                content = gson.toJson(message.content).getBytes(StandardCharsets.UTF_8);
            else
                content = gson.toJson(new JsonObject()).getBytes(StandardCharsets.UTF_8);

            final byte[][] data = { header, parentHeader, metadata, content };
            final SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(keySpec);
            for (final byte[] d : data)
                mac.update(d);
            final byte[] signature = DatatypeConverter.printHexBinary(mac.doFinal()).replaceAll("-", "").toLowerCase()
                    .getBytes(StandardCharsets.UTF_8);

            zMsg.add(signature);
            zMsg.add(header);
            zMsg.add(parentHeader);
            zMsg.add(metadata);
            zMsg.add(content);
            return zMsg.send(socket);
        }

        void sendStatus(Message parentMessage, String status) throws Exception
        {
            final Message busyStatus = new Message(parentMessage, "status");
            busyStatus.content.put("execution_state", status);
            send(busyStatus);
        }
    }

    static class ConnectionSettings
    {
        @SerializedName("ip")
        String address;
        @SerializedName("control_port")
        int    controlPort;
        @SerializedName("hb_port")
        int    hbPort;
        @SerializedName("iopub_port")
        int    iopubPort;
        String key;
        @SerializedName("shell_port")
        int    shellPort;
        @SerializedName("signature_scheme")
        String signatureScheme;
        @SerializedName("stdin_port")
        int    stdinPort;
        String transport;
    }

    class ControlChannel extends Channel
    {
        ControlChannel()
        {
            super(settings.address, settings.controlPort, ZMQ.ROUTER);
        }
    }

    class Header
    {
        String date;
        @SerializedName("msg_id")
        String messageId;
        @SerializedName("msg_type")
        String messageType;
        @SerializedName("session")
        String sessionId;
        String username;
        String version;

        Header()
        {
        }

        Header(String messageType)
        {
            date = DF.format(new Date());
            messageId = UUID.randomUUID().toString();
            this.messageType = messageType;
            sessionId = id;
            username = "kernel";
            version = "5.0";
        }
    }

    class HeartBeatChannel extends Channel
    {
        HeartBeatChannel()
        {
            super(settings.address, settings.hbPort, ZMQ.REP);
        }

        @Override
        public void run()
        {
            while (true)
                ZMQ.proxy(socket, socket, null);
        }
    }

    private class InputChannel extends Channel
    {
        InputChannel()
        {
            super(settings.address, settings.stdinPort, ZMQ.ROUTER);
        }
    }

    class Message
    {
        transient List<byte[]> buffers;
        Map<String, Object>    content;
        Header                 header;
        transient List<String> identities;
        Map<String, Object>    metadata;
        @SerializedName("parent_header")
        Header                 parentHeader;

        Message()
        {
        }

        Message(Message parentMessage, String messageType)
        {
            header = new Header(messageType);
            parentHeader = parentMessage.header;
            metadata = new HashMap<>();
            content = new HashMap<>();
        }

        Message(String messageType)
        {
            header = new Header(messageType);
            metadata = new HashMap<>();
            content = new HashMap<>();
        }
    }

    class OutputChannel extends Channel
    {
        OutputChannel()
        {
            super(settings.address, settings.iopubPort, ZMQ.PUB);
        }
    }

    class ShellChannel extends Channel
    {
        int globalExecutionCount = 1;

        ShellChannel()
        {
            super(settings.address, settings.shellPort, ZMQ.ROUTER);
        }

        void doExecuteRequest(Message message) throws Exception
        {
            outputChannel.sendStatus(message, "busy");

            final Message executeInput = new Message(message, "execute_input");
            executeInput.content.put("execution_count", globalExecutionCount);
            executeInput.content.put("code", message.content.get("code"));

            outputChannel.send(executeInput);

            final Message stream = new Message(message, "stream");
            stream.content.put("name", "stdout");
            outputChannel.send(stream);

            final Message executeResult = new Message(message, "execute_result");
            executeResult.content.put("execution_count", globalExecutionCount);
            final Map<String, Object> data = new HashMap<>();

            try (DXScriptEngine engine = UnityDXSystem.getLocalEngineByName("basex")) {
                final Object result = engine.eval(message.content.get("code").toString());
                if (result instanceof double[]) {
                    final StringBuilder sb = new StringBuilder();
                    final double[] rr = (double[]) result;
                    for (final double r : rr)
                        sb.append(r + ",");
                    data.put("text/plain", sb.toString());
                } else if (result instanceof Object[]) {
                    final Object[] rr = (Object[]) result;
                    final List<String> d1 = new LinkedList<>();
                    final List<String> d2 = new LinkedList<>();
                    for (int i = 0; i < rr.length; i++)
                        if (rr[i] instanceof BufferedImage)
                            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                                ImageIO.write((BufferedImage) rr[i], "png", baos);
                                baos.flush();
                                d1.add(new String(Base64.getEncoder().encode(baos.toByteArray()),
                                        StandardCharsets.UTF_8));
                            }
                        else
                            d2.add(rr[i].toString());
                    if (!d1.isEmpty())
                        data.put("image/png", d1.toArray());
                    if (!d2.isEmpty())
                        data.put("text/plain", d2.toArray());

                } else if (result instanceof BufferedImage)
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        ImageIO.write((BufferedImage) result, "png", baos);
                        baos.flush();
                        data.put("image/png",
                                new String(Base64.getEncoder().encode(baos.toByteArray()), StandardCharsets.UTF_8));
                    }
                else
                    data.put("text/plain", result.toString());
            } catch (final Exception e) {
                final StringWriter sw = new StringWriter();
                final PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                data.put("text/plain", sw.toString());
            }

            executeResult.content.put("data", data);
            executeResult.content.put("metadata", new HashMap<>());
            outputChannel.send(executeResult);

            outputChannel.sendStatus(message, "idle");

            final Message executeReply = new Message(message, "execute_reply");
            executeReply.content.put("status", "ok");
            executeReply.content.put("output_type", "execute_result");
            executeReply.content.put("execution_count", globalExecutionCount);
            executeReply.content.put("user_variables", new HashMap<>());
            executeReply.content.put("payload", new ArrayList<>());
            final HashMap<String, Object> ex = new HashMap<>();
            ex.put("abc", 123);
            executeReply.content.put("user_expressions", ex);
            executeReply.identities = message.identities;

            send(executeReply);
            globalExecutionCount++;
        }

        void doKernelInfoRequest(Message message) throws Exception
        {
            final Message kernelInfoReply = new Message("kernel_info_reply");
            kernelInfoReply.identities = message.identities;
            kernelInfoReply.content.put("protocol_version", "5.0");
            kernelInfoReply.content.put("implementation", "unity-kernel");
            kernelInfoReply.content.put("implementation_version", "0.0.1");

            final Map<String, Object> languageInfo = new HashMap<>();
            languageInfo.put("name", "unity-kernel");
            languageInfo.put("version", "0.0.1");
            languageInfo.put("mimetype", "application/xquery");
            languageInfo.put("file_extension", ".xq");
            languageInfo.put("pygments_lexer", "xquery");
            languageInfo.put("codemirror_mode", "xquery");
            languageInfo.put("nbconvert_exporter", "");

            kernelInfoReply.content.put("language_info", languageInfo);
            kernelInfoReply.content.put("banner", "Unity Kernel");
            // kernelInfoReply.content.put("status", "ok");

            send(kernelInfoReply);
            outputChannel.sendStatus(message, "idle");
        }

        void doShutdownRequest(Message message)
        {
        }

        @Override
        void onMessage(Message message) throws Exception
        {
            switch (message.header.messageType) {
            case "execute_request":
                doExecuteRequest(message);
                break;
            case "kernel_info_request":
                doKernelInfoRequest(message);
                break;
            case "history_request":
                break;
            case "object_info_request":
                break;
            case "complete_request":
                break;
            case "comm_msg":
                break;
            case "shutdown_request":
                doShutdownRequest(message);
                break;
            default:
            }
        }
    }
}
