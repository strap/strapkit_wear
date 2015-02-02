package com.straphq.strapkit.framework.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by martinza on 2/2/15.
 */
public class PluginManager {

    private static final String TAG = PluginManager.class.getSimpleName();

    private Context mContext;
    private HashMap<String, PluginInfo> plugins = new HashMap<>();

    public PluginManager(Context context) {
        mContext = context;
    }

    public void init() {
        ArrayList<PluginInfo> pluginInfoList = JsonConfigParser.getPluginInfo(mContext);
        for (PluginInfo pluginInfo : pluginInfoList) {
            plugins.put(pluginInfo.serviceName, pluginInfo);
        }
        Log.d(TAG, "plugin size: " + plugins.size());
    }

    public String getPackageInfo(String serviceName) {
        Log.d(TAG, "plugin size: " + plugins.size());
        PluginInfo pluginInfo = plugins.get(serviceName);
        if (pluginInfo != null) {
            return pluginInfo.packageInfo;
        } else {
            Log.d(TAG, "Failed to find service: " + serviceName);
            return null;
        }
    }

    public static class PluginInfo {
        public String serviceName;
        public String packageInfo;
    }
}
