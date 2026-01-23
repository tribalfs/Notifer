package de.dlyt.yanndroid.notifer.utils;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import de.dlyt.yanndroid.notifer.model.NotiPacket;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {

    public static void postAll(List<Preferences.ServerInfo> servers, NotiPacket notification) {
        OkHttpClient client = new OkHttpClient();
        for (Preferences.ServerInfo server : servers) {
            try {
                String body = notification.toJsonString(server.inclContent);
                if (!server.secretKey.isEmpty()) {
                    Pair<String, String> iv_ciphertext = Crypto.encrypt(body, server.secretKey);
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("iv", iv_ciphertext.first);
                    jsonBody.put("body", iv_ciphertext.second);
                    body = jsonBody.toString();
                }
                post(client, server.url, body);
            } catch (JSONException | GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private static void post(OkHttpClient client, String url, String body) {
        new Thread(() -> {
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(body, MediaType.parse("application/json; charset=utf-8")))
                    .build();

            try (Response response = client.newCall(request).execute()) {
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
