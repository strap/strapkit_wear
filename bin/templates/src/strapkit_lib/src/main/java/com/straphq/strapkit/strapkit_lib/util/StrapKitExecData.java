package com.straphq.strapkit.strapkit_lib.util;

import com.google.android.gms.wearable.DataMap;

/**
 * Created by martinza on 2/2/15.
 */
public class StrapKitExecData {

    private String mService;
    private String mAction;
    private String mCallbackId;
    private String mArgsJson;

    public StrapKitExecData(String service, String action, String callbackId, String argsJson) {
        mService = service;
        mAction = action;
        mCallbackId = callbackId;
        mArgsJson = argsJson;
    }

    public void setDataMap(DataMap dataMap) {
        dataMap.putString("service", mService);
        dataMap.putString("action", mAction);
        dataMap.putString("callbackId", mCallbackId);
        dataMap.putString("argsJson", mArgsJson);
    }
}
