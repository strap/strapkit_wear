package com.straphq.strapkit.framework.util;

import com.google.android.gms.wearable.DataMap;

import java.io.Serializable;

/**
 * Created by martinza on 2/3/15.
 */
public class PluginResult implements Serializable {

    public static String[] StatusMessages = new String[] {
            "No result",
            "OK",
            "Class not found",
            "Illegal access",
            "Instantiation error",
            "Malformed url",
            "IO error",
            "Invalid action",
            "JSON error",
            "Error"
    };

    public enum Status {
        NO_RESULT,
        OK,
        CLASS_NOT_FOUND_EXCEPTION,
        ILLEGAL_ACCESS_EXCEPTION,
        INSTANTIATION_EXCEPTION,
        MALFORMED_URL_EXCEPTION,
        IO_EXCEPTION,
        INVALID_ACTION,
        JSON_EXCEPTION,
        ERROR
    }


    private Integer mStatus;
    private String mJsonMessage;

    public PluginResult(Status status) {
        this(status, PluginResult.StatusMessages[status.ordinal()]);
    }

    public PluginResult(Status status, String message) {
        mJsonMessage = message;
        mStatus = status.ordinal();
    }

    public Integer getStatus() {
        return mStatus;
    }

    public String getMessage() {
        return mJsonMessage;
    }

    public void setDataMap(String callbackId, DataMap dataMap) {
        dataMap.putString("callbackId", callbackId);
        dataMap.putInt("status", mStatus);
        dataMap.putString("message", mJsonMessage);
    }
}
