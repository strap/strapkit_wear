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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    //JS Interface methods


    @JavascriptInterface
    public void showPage(String json) {
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("views");
            Log.d(TAG, "show Page: " + json);
        } catch (Exception e) {
            Log.d(TAG, "failed to parse", e);

        }
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
                String html = "<html><script type=\"text/javascript\" src=\"file:///android_asset/js/app.min.js\"></script><body></body></html>";
                mWebView.loadDataWithBaseURL("file:////android_asset/", html, "text/html", "utf-8", "");
                //mWebView.loadUrl("javascript:strapkit.init()");
            }
        });
    }
}
