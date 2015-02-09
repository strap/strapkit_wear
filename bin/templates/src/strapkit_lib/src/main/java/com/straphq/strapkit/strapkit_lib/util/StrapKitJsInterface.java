package com.straphq.strapkit.strapkit_lib.util;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.gms.wearable.DataMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.straphq.strapkit.strapkit_lib.messaging.StrapKitMessageService;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by martinza on 12/31/14.
 */
public class StrapKitJsInterface {

    StrapKitMessageService mService;
    WebView mWebView;
    private Handler handler = new Handler();

    private static final String TAG = StrapKitJsInterface.class.getSimpleName();
    private boolean loaded = false;

    /**
     * Instantiate the interface and set the context
     */
    public StrapKitJsInterface(StrapKitMessageService c) {
        mService = c;
    }

    public void startWebView() {
        mWebView = new WebView(mService);
        WebSettings settings;
        settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        //We don't save any form data in the application
        settings.setSaveFormData(false);

        // Enable DOM storage
        settings.setDomStorageEnabled(true);

        // Enable built-in geolocation
        settings.setGeolocationEnabled(true);

        // Enable AppCache
        // Fix for CB-2282
        settings.setAppCacheEnabled(true);

        settings.setAllowFileAccessFromFileURLs(true);

        settings.getUserAgentString();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.addJavascriptInterface(this, "strapkit_bridge");

        init();
    }


    @JavascriptInterface
    public String exec(String service, String action, String callbackId, String argsJson) {
        Log.d(TAG, "service: " + service + ", action: " + action + ", callbackId: " + callbackId + ", argsJson: " + argsJson);
        StrapKitExecData data = new StrapKitExecData(service, action, callbackId, argsJson);
        mService.sendData("/exec", data);
        return null;
    }

    public void init() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("file:///android_asset/index.html");
                StrapKitJsInterface.this.loaded = true;
            }
        });
    }

    public void sendCallback(DataMap map) {

        final StringBuilder sb = new StringBuilder();
        sb.append("javascript:window.callbacks.callbackFromNative(");
        sb.append("'" + map.getString("callbackId") + "', ");
        if (map.getInt("status") == 1) {
            sb.append("true, ");
        } else {
            sb.append("false, ");
        }
        sb.append(map.getInt("status") + ", ");
        sb.append("'" + map.getString("message") + "', ");
        sb.append("true);");

        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Javascript: " + sb.toString());
                mWebView.loadUrl(sb.toString());
            }
        });
    }
}
