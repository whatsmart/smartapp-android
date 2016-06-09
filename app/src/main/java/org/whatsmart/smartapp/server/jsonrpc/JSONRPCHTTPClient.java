package org.whatsmart.smartapp.server.jsonrpc;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by blue on 2016/6/8.
 */
public class JSONRPCHTTPClient {
    //url必须是http协议
    public String apiAddress;
    public static int id = 0;

    public JSONRPCHTTPClient(String apiAddress) {
        this.apiAddress = apiAddress;
    }

    public String buildRequest(String method, JsonElement params) {
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("jsonrpc", "2.0");
        request.addProperty("method", method);
        request.add("params", params);
        request.addProperty("id", nextID());
        return gson.toJson(request);
    }

    public String buildRequest(String method) {
        Gson gson = new Gson();
        JsonObject request = new JsonObject();
        request.addProperty("jsonrpc", "2.0");
        request.addProperty("method", method);
        request.addProperty("id", nextID());
        return gson.toJson(request);
    }

    public String getResponse(String resource, String request) {
        try {
            URL url = new URL(apiAddress + resource);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setRequestMethod("POST");
            httpConn.setConnectTimeout(20000);
            httpConn.setReadTimeout(20000);
            httpConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpConn.setRequestProperty("Accept-Encoding", "identity");
            OutputStream outputStream = httpConn.getOutputStream();
            outputStream.write(request.getBytes());
            httpConn.connect();

            int statusCode = httpConn.getResponseCode();
            int length = httpConn.getContentLength();

            System.out.println(request);

            if (statusCode == HttpURLConnection.HTTP_OK) {
                BufferedInputStream inputStream = new BufferedInputStream(httpConn.getInputStream());
                InputStreamReader inputReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputReader);

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                    builder.append(System.getProperty("line.separator"));
                }
                bufferedReader.close();
                return builder.toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void call(final String resource, final String method, final JSONRPCResponse.ResponseCallback callback) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String request = buildRequest(method);
                    String response = getResponse(resource, request);

                    if (response != null) {
                        JsonElement jsonElement = new JsonParser().parse(response);
                        if (jsonElement instanceof JsonObject) {
                            JsonElement result = ((JsonObject) jsonElement).get("result");
                            JsonElement error = ((JsonObject) jsonElement).get("error");
                            if (result != null) {
                                callback.onResult(result);
                            } else if (error != null) {
                                callback.onError(error);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void call(final String resource, final String method, final JsonElement params, final JSONRPCResponse.ResponseCallback callback) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        String request = buildRequest(method, params);
                        String response = getResponse(resource, request);

                        if (response != null) {
                            JsonElement jsonElement = new JsonParser().parse(response);
                            if (jsonElement instanceof JsonObject) {
                                JsonElement result = ((JsonObject) jsonElement).get("result");
                                JsonElement error = ((JsonObject) jsonElement).get("error");
                                if (result != null) {
                                    callback.onResult(result);
                                } else if (error != null) {
                                    callback.onError(error);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
    }

    public static int nextID() {
        id += 1;
        if (id == Integer.MAX_VALUE) {
            id = 0;
        }
        return id;
    }
}
