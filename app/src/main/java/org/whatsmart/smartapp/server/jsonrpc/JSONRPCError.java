package org.whatsmart.smartapp.server.jsonrpc;

/**
 * Created by blue on 2016/6/9.
 */
public class JSONRPCError {
    public int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message;
    public Object data;
}
