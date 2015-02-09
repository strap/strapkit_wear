package com.straphq.strapkit.framework.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.straphq.strapkit.framework.StrapKitApplication;
import com.straphq.strapkit.framework.messaging.StrapKitWearListener;

/**
 * Created by martinza on 2/2/15.
 */
public class Callback {

    private static final String TAG = Callback.class.getSimpleName();

    private String mCallbackId;
    private Context mContext;

    public Callback(String callbackId, Context context) {
        mCallbackId = callbackId;
        mContext = context;
    }

    public String getCallbackId() {
        return mCallbackId;
    }

    public void sendPluginResult(PluginResult result) {
        Log.d(TAG, "Sending callback plugin result");
        Intent intent = new Intent();
        intent.setAction(StrapKitWearListener.RECEIVER_CALLBACK);
        intent.putExtra(StrapKitWearListener.PLUGIN_RESULT, result);
        intent.putExtra(StrapKitWearListener.CALLBACK_ID, mCallbackId);
        mContext.sendBroadcast(intent);
    }

    public void error(String message) {
        sendPluginResult(new PluginResult(PluginResult.Status.ERROR, message));
    }

    public void success(String jsonMessage) {
        sendPluginResult(new PluginResult(PluginResult.Status.OK, jsonMessage));
    }


}
