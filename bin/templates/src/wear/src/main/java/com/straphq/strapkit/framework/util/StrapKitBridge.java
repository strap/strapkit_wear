package com.straphq.strapkit.framework.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.straphq.strapkit.framework.StrapKitApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by martinza on 1/6/15.
 */
public class StrapKitBridge {

    private static final String TAG = StrapKitBridge.class.getSimpleName();

    public static StrapKitApplication getApplication(Context context) {
        return (StrapKitApplication) context.getApplicationContext();
    }

    public static void handleMessage(Context context, String path, DataMap dataMap) {
        Log.d(TAG, "path: " + path);
        switch (path){
            case "exec":
                executeService(context, dataMap);
                break;
            default:
                break;
        }
    }

    private static void executeService(Context context, DataMap dataMap) {
        Log.d(TAG, "service: " + dataMap.getString("service"));

        String packageInfo = getApplication(context).getPluginManager().getPackageInfo(dataMap.getString("service"));

        Log.d(TAG, "packageInfo: " + packageInfo);

        try {
            Class<?> pluginClass = Class.forName(packageInfo);
            Method method = pluginClass.getMethod("exec", String.class, String.class, Integer.class);
            method.invoke(pluginClass, dataMap.getString("action"), dataMap.getString("argsJson"), dataMap.getInt("callbackId"));

        } catch (ClassNotFoundException e) {
            Log.d(TAG, "No Plugin is found for: " + packageInfo, e);
        } catch (NoSuchMethodException e) {
            Log.d(TAG, "No method seen for exec(String, String, Int)", e);
        } catch (InvocationTargetException e) {
            Log.d(TAG, "Failed to invoke method: exec", e);
        } catch (IllegalAccessException e) {
            Log.d(TAG, "Cannot access exec", e);
        }


    }
}
