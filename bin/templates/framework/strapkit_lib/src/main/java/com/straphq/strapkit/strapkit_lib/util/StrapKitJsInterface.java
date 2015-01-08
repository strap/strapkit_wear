package com.straphq.strapkit.strapkit_lib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.straphq.strapkit.strapkit_lib.messaging.StrapKitMessageService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by martinza on 12/31/14.
 */
public class StrapKitJsInterface {

    StrapKitMessageService mService;
    WebView mWebView;
    private Handler handler = new Handler();

    boolean metricsEnabled = false;

    private static final String TAG = StrapKitJsInterface.class.getSimpleName();

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
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        //We don't save any form data in the application
        settings.setSaveFormData(false);
        settings.setSavePassword(false);


        // Enable DOM storage
        settings.setDomStorageEnabled(true);

        // Enable built-in geolocation
        settings.setGeolocationEnabled(true);

        // Enable AppCache
        // Fix for CB-2282
        settings.setAppCacheMaxSize(5 * 1048576);
        settings.setAppCacheEnabled(true);

        settings.setAllowFileAccessFromFileURLs(true);

        // Fix for CB-1405
        // Google issue 4641
        WebView.setWebContentsDebuggingEnabled(true);
        settings.getUserAgentString();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.addJavascriptInterface(this, "strapkit_bridge");
        init();
    }


    @JavascriptInterface
    public void showPage(String json) {
        try {
            mService.sendMessage(StrapKitConstants.ACTION_SHOW_PAGE, json);
            Log.d(TAG, "show Page: " + json);
        } catch (Exception e) {
            Log.d(TAG, "failed to parse", e);

        }
    }

    @JavascriptInterface
    public void httpClient(String options, String success, String failure) {
        Gson gson = new Gson();

        HttpClient httpClient = gson.fromJson(options, HttpClient.class);

        httpClient.setSuccess(success);
        httpClient.setFailure(failure);

    }

    public void log(String msg) {
        Log.d("Strapkit", msg);
    }

    public void init() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String html = "<html><script type=\"text/javascript\" src=\"file:///android_asset/js/lib/klass.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/page.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/view.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/card.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/httpClient.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/list.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/text.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/strapkit.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/app-example.js\"></script><body></body></html>";
                mWebView.loadDataWithBaseURL("file:////android_asset/", html, "text/html", "utf-8", "");
                //mWebView.loadUrl("javascript:strapkit.init()");
            }
        });
    }

    public void evaluateJs(final String javascript) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "javascript: " + javascript);
                mWebView.loadUrl("javascript:var myMethod = " + javascript + "; myMethod();");
            }
        });
    }
}

