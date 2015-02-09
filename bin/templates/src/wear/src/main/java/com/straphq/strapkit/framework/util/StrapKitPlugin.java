package com.straphq.strapkit.framework.util;

import android.content.Context;

import org.json.JSONException;

/**
 * Created by martinza on 2/2/15.
 */
public class StrapKitPlugin {

    private Context mContext;


    public void setContext(Context context) {
        mContext = context;

    }

    public Context getContext() {
        return mContext;
    }

    public boolean execute(String action, String argsJson, Callback callback) throws JSONException {
        return true;
    }
}
