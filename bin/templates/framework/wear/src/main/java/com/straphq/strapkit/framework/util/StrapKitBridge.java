package com.straphq.strapkit.framework.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.straphq.strapkit.framework.StrapKitApplication;
import com.straphq.strapkit.framework.StrapKitBaseActivity;
import com.straphq.strapkit.framework.view.StrapKitBaseView;
import com.straphq.strapkit.framework.view.StrapKitCardFragment;
import com.straphq.strapkit.framework.view.StrapKitListView;
import com.straphq.strapkit.framework.view.StrapKitTextView;
import com.straphq.strapkit.strapkit_lib.util.StrapKitConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by martinza on 1/6/15.
 */
public class StrapKitBridge {

    private static final String TAG = StrapKitBridge.class.getSimpleName();


    public static void handleMessage(Context context, String path, String json) {
        Log.d(TAG, "path: " + path + ", json: " + json);
        switch (path){
            case StrapKitConstants.ACTION_SHOW_PAGE:
                try {
                    JsonParser parser = new JsonParser();
                    JsonArray array = parser.parse(json).getAsJsonObject().getAsJsonArray("views");
                    showPage(context, array);
                } catch (Exception e) {
                    Log.d(TAG, "Failed to parse json: " + json, e);
                }
                break;
            default:
                break;
        }
    }


    public static void showPage(Context context, JsonArray array) throws JSONException {
        Gson gson = new Gson();
        ArrayList<StrapKitBaseView> activityViews = new ArrayList<>();
        for (JsonElement element : array) {
            JsonObject object = element.getAsJsonObject();
            switch (object.get("type").getAsString()) {
                case "card":
                    Log.d(TAG, "element: " + element.toString());
                    StrapKitCardFragment cardFragment = gson.fromJson(object, StrapKitCardFragment.class);
                    activityViews.add(cardFragment);
                    break;
                case "text":
                    Log.d(TAG, "element: " + element.toString());
                    StrapKitTextView textView = gson.fromJson(object, StrapKitTextView.class);
                    activityViews.add(textView);
                    break;
                case "listView":
                    Log.d(TAG, "element: " + element.toString());
                    gson = new GsonBuilder()
                            .registerTypeAdapter(StrapKitListView.WearableItem.class, new StrapKitListView.Desirializer()).create();
                    StrapKitListView listView = gson.fromJson(object, StrapKitListView.class);
                    activityViews.add(listView);
                    break;
                default:
                    Log.d(TAG, "Object not supported: " + object.get("type").getAsString());
                    break;
            }
        }

        Intent intent = new Intent(context, StrapKitBaseActivity.class);
        intent.putExtra(StrapKitBaseActivity.ARGS_VIEW_DEFINITIONS, activityViews);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((StrapKitApplication) context.getApplicationContext()).launchNewActivity(intent);
    }
}
