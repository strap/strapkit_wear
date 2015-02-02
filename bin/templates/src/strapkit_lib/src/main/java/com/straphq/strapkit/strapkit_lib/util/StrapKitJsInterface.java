package com.straphq.strapkit.strapkit_lib.util;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

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

    public void evaluateJs(final String javascript) {
        evaluateJs(javascript, null);
    }

    public void evaluateJs(final String javascript, final String info) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!StrapKitJsInterface.this.loaded) return;
                    String javascriptNoComments = javascript;
                    String javascriptFunction = "javascript:var myMethod = " + javascriptNoComments + ";";
                    if (info != null) {
                        try {
                            String cleanedArgs = info.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
                            JSONObject object = new JSONObject(cleanedArgs);
                            javascriptFunction = "javascript: var data = JSON.parse('" + object.toString() + "'); \n(" + javascriptNoComments + ")(data);";
                        } catch (Exception o) {
                            Log.d(TAG, "not an object");
                            try {
                                JSONArray array = new JSONArray(info);
                                javascriptFunction = "javascript: var data = JSON.parse('" + array.toString() + "'); \n(" + javascriptNoComments + ")(data);";
                            } catch (Exception a) {
                                Log.d(TAG, "not array");
                                javascriptFunction = "javascript:(" + javascriptNoComments + ")(" + info + ");";
                            }
                        }
                    } else {
                        javascriptFunction = javascriptFunction + " \n myMethod();";
                    }
                    Log.d(TAG, javascriptFunction);
                    mWebView.loadUrl(javascriptFunction);
                } catch (Exception e) {
                    Log.d(TAG, "exception", e);
                }
            }
        });
    }
}
