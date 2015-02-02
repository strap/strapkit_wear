package com.straphq.strapkit.framework.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by martinza on 2/2/15.
 */
public class JsonConfigParser {

    private static final String TAG = JsonConfigParser.class.getSimpleName();

    private static final String CONFIG_FILE_NAME = "strapkit_plugins.json";

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {

            InputStream is = context.getAssets().open(CONFIG_FILE_NAME);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            Log.d(TAG, "Issue parsing: " + CONFIG_FILE_NAME);
            return null;
        }
        return json;
    }

    public static ArrayList<PluginManager.PluginInfo> getPluginInfo(Context context) {
        ArrayList<PluginManager.PluginInfo> pluginInfoList = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        String json = loadJSONFromAsset(context);
        Log.d(TAG, "json: " + json);
        JsonArray array = parser.parse(json).getAsJsonArray();
        for (JsonElement element : array) {
            JsonObject obj = element.getAsJsonObject();
            if (obj.has("android-wear")) {
                JsonArray pluginArray = obj.getAsJsonArray("android-wear");
                for (JsonElement pluginElement : pluginArray) {
                    PluginManager.PluginInfo pluginInfo = gson.fromJson(pluginElement, PluginManager.PluginInfo.class);

                    Log.d(TAG, "serviceName: " + pluginInfo.serviceName + ", packageInfo: " + pluginInfo.serviceName);
                    pluginInfoList.add(pluginInfo);
                }
            }
        }
        return pluginInfoList;
    }
}
