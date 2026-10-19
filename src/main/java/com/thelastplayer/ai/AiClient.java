package com.thelastplayer.ai;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thelastplayer.EchoConfig;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Minimal OpenAI-compatible chat client (Java 8 safe: HttpURLConnection, no extra libs). */
public final class AiClient {
    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "echo-ai");
        t.setDaemon(true);
        return t;
    });

    private AiClient() {}

    /** history = list of {role, content}. Completes exceptionally on any failure. */
    public static CompletableFuture<String> chat(String system, List<String[]> history, String userMsg) {
        final String endpoint = EchoConfig.AI_ENDPOINT.get();
        final String key = EchoConfig.apiKey();
        final String model = EchoConfig.AI_MODEL.get();
        final int timeout = EchoConfig.AI_TIMEOUT.get() * 1000;

        return CompletableFuture.supplyAsync(() -> {
            HttpURLConnection con = null;
            try {
                JsonObject body = new JsonObject();
                body.addProperty("model", model);
                body.addProperty("max_tokens", 120);
                body.addProperty("temperature", 0.85);
                JsonArray msgs = new JsonArray();
                msgs.add(msg("system", system));
                for (String[] h : history) msgs.add(msg(h[0], h[1]));
                msgs.add(msg("user", userMsg));
                body.add("messages", msgs);

                con = (HttpURLConnection) new URL(endpoint).openConnection();
                con.setRequestMethod("POST");
                con.setConnectTimeout(timeout);
                con.setReadTimeout(timeout);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                if (!key.isEmpty()) con.setRequestProperty("Authorization", "Bearer " + key);
                try (OutputStream os = con.getOutputStream()) {
                    os.write(body.toString().getBytes(StandardCharsets.UTF_8));
                }
                int code = con.getResponseCode();
                InputStream in = code >= 200 && code < 300 ? con.getInputStream() : con.getErrorStream();
                String resp = read(in);
                if (code < 200 || code >= 300) throw new RuntimeException("HTTP " + code);
                JsonObject o = new JsonParser().parse(resp).getAsJsonObject();
                return o.getAsJsonArray("choices").get(0).getAsJsonObject()
                        .getAsJsonObject("message").get("content").getAsString();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                if (con != null) con.disconnect();
            }
        }, EXEC);
    }

    private static JsonObject msg(String role, String content) {
        JsonObject m = new JsonObject();
        m.addProperty("role", role);
        m.addProperty("content", content);
        return m;
    }

    private static String read(InputStream in) throws Exception {
        if (in == null) return "";
        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf)) > 0) bos.write(buf, 0, n);
        return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }
}
