package com.straphq.strapkit.strapkit_lib.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by martinza on 12/31/14.
 */
public class StrapKitJsInterface {

    Context mContext;
    WebView mWebView;
    private Handler handler = new Handler();

    boolean metricsEnabled = false;

    private static final String TAG = StrapKitJsInterface.class.getSimpleName();


    /** Instantiate the interface and set the context */
    public StrapKitJsInterface(Context c) {
        mContext = c;
    }

    public void startWebView() {
        mWebView = new WebView(mContext);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.addJavascriptInterface(this, "strapkit_bridge");
        init();
    }

    private String getJavascript() {
        String out = "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(mContext.getAssets().open("app.min.js")));
            String str;
            while ((str = in.readLine()) != null) {
                out += str;
            }
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return out;
    }

    //JS Interface methods


    //StrapMetrics methods
    @JavascriptInterface
    public void strapMetricsInit(String appID) {
        Log.d(TAG, "strapMetricsInit: " + appID);
    }

    @JavascriptInterface
    public void strapMetricsLogEvent(String event, String cvar) {
        Log.d(TAG, "strapMetricsLogEvent: " + event);
    }

    @JavascriptInterface
    public void setListView(String title, String viewID, String listJSON) {
        Log.d(TAG, "setListView");

    }

    @JavascriptInterface
    public void setTextView(String viewText) {
        Log.d(TAG, "setTextView");

    }

    //Sensor methods

    //Android-specific methods
    @JavascriptInterface
    public void  confirmActivity(String message) {
        Log.d(TAG, "confirmActivity");
    }

    public void log(String msg) {
        Log.d("Strapkit", msg);
    }

    public void init() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                String html = "<html><script src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js\"></script><script src=\"file:///android_asset/require.js\"></script><script src=\"file:///android_asset/ajax.js\"></script><script src=\"file:///android_asset/strapkit.js\"></script><script src=\"file:///android_asset/app.js\"></script><body></body></html>";
                mWebView.loadDataWithBaseURL("file:////android_asset/", html, "text/html", "utf-8", "");
                //mWebView.loadUrl("javascript:strapkit.init()");
            }
        });
    }
}
