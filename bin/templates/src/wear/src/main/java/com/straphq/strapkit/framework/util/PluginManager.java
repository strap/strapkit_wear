package com.straphq.strapkit.framework.util;

import android.content.Context;
import android.util.Log;

import com.straphq.strapkit.framework.StrapKitApplication;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by martinza on 2/2/15.
 */
public class PluginManager {

    private static final String TAG = PluginManager.class.getSimpleName();
    private static final long SLOW_EXEC_WARNING_THRESHOLD = 30;

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

    public void exec(final String service, final String action, final String callbackId, final String rawArgs) {

        StrapKitPlugin plugin = getPlugin(service);

        // Plugin does not exist in Android
        if (plugin == null) {
            Log.d(TAG, "exec() call to unknown plugin: " + service);

            Callback callback = new Callback(callbackId, mContext);
            callback.sendPluginResult(new PluginResult(PluginResult.Status.CLASS_NOT_FOUND_EXCEPTION));
            return;
        }

        Callback callback = new Callback(callbackId, mContext);
        try {
            long pluginStartTime = System.currentTimeMillis();
            boolean wasValidAction = plugin.execute(action, rawArgs, callback);
            long duration = System.currentTimeMillis() - pluginStartTime;

            if (duration > SLOW_EXEC_WARNING_THRESHOLD) {
                Log.w(TAG, "THREAD WARNING: exec() call to " + service + "." + action + " blocked the main thread for " + duration + "ms. Plugin should use CordovaInterface.getThreadPool().");
            }
            if (!wasValidAction) {
                PluginResult cr = new PluginResult(PluginResult.Status.INVALID_ACTION);
                callback.sendPluginResult(cr);
            }
        } catch (JSONException e) {
            PluginResult cr = new PluginResult(PluginResult.Status.JSON_EXCEPTION);
            callback.sendPluginResult(cr);
        } catch (Exception e) {
            Log.e(TAG, "Uncaught exception from plugin", e);
            callback.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "Uncaught exception from plugin: " + e.getMessage()));
        }
    }

    private StrapKitPlugin getPlugin(String serviceName) {
        try {
            String packageInfo = getPackageInfo(serviceName);
            Class<?> pluginClass = Class.forName(packageInfo);
            StrapKitPlugin plugin = (StrapKitPlugin) pluginClass.newInstance();
            plugin.setContext(mContext);
            return plugin;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
