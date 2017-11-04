package io.dgms.unity.kernel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.xml.bind.DatatypeConverter;

import org.basex.core.Context;
import org.basex.io.serial.Serializer;
import org.basex.query.QueryProcessor;
import org.basex.query.value.item.Item;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import io.dgms.unity.UnityDGSession;
import io.dgms.unity.UnityDGSessionObject;
import io.dgms.unity.modules.engines.basex.BaseXDGScriptEngine;
import io.dgms.unity.modules.engines.basex.BaseXDGScriptEngineFactory;

public class UnityDGKernel extends UnityDGSessionObject implements AutoCloseable
{
    private final ZMQ.Context        context;
    private final ControlChannel     controlChannel;
    private final HeartBeatChannel   heartBeatChannel;
    private final InputChannel       inputChannel;
    private final OutputChannel      outputChannel;
    private final AtomicBoolean      running;
    private final ConnectionSettings settings;
    private final ShellChannel       shellChannel;

    public UnityDGKernel(Path connectionFile) throws IOException
    {
        this(null, connectionFile);
    }

    public UnityDGKernel(UnityDGSession session, Path connectionFile) throws IOException
    {
        super(session);
        try (Reader reader = Files.newBufferedReader(connectionFile)) {
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
            final byte[] signature = DatatypeConverter.printHexBinary(mac.doFinal()).toLowerCase()
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

    static class Header
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

        Header(String messageType, String sessionId, String username)
        {
            date = DF.format(new Date());
            messageId = UUID.randomUUID().toString();
            this.messageType = messageType;
            this.sessionId = sessionId;
            this.username = username;
            version = "5.0";
        }

        static DateFormat DF;

        static {
            DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
            DF.setTimeZone(TimeZone.getTimeZone("UTC"));
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

    static class Message
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
            header = new Header(messageType, parentMessage.header.sessionId, parentMessage.header.username);
            parentHeader = parentMessage.header;
            metadata = new HashMap<>();
            content = new HashMap<>();
        }

        Message(String messageType, String sessionId, String username)
        {
            header = new Header(messageType, sessionId, username);
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
        int globalExecutionCount;

        ShellChannel()
        {
            super(settings.address, settings.shellPort, ZMQ.ROUTER);
        }

        @Override
        void onMessage(Message message) throws Exception
        {
            if ("kernel_info_request".equals(message.header.messageType)) {
                final Message kernelInfoReply = new Message(message, "kernel_info_reply");
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
                kernelInfoReply.content.put("banner", "Unity DGMS Kernel");
                send(kernelInfoReply);
                outputChannel.sendStatus(message, "idle");
            } else if ("execute_request".equals(message.header.messageType)) {

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

                try (BaseXDGScriptEngine engine = new BaseXDGScriptEngineFactory().getScriptEngine()) {
                    final Object result = engine.eval(message.content.get("code").toString());
                    data.put("text/plain", result.toString());
                }

                executeResult.content.put("data", data);
                executeResult.content.put("metadata", new HashMap<>());
                outputChannel.send(executeResult);

                outputChannel.sendStatus(message, "idle");

                final Message executeReply = new Message(message, "execute_reply");
                executeReply.content.put("status", "ok");
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
        }
    }
}
