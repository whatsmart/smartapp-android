package org.whatsmart.smartapp.server.jsonrpc;

import com.google.gson.JsonElement;

/**
 * Created by blue on 2016/6/8.
 */
public class JSONRPCResponse {
    public interface ResponseCallback {
        public abstract void onResult(JsonElement result);
        public abstract void onError(JsonElement error);
    }
}