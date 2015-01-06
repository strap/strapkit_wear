package com.straphq.strapkit.strapkit_lib.util;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

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

    boolean metricsEnabled = false;

    private static final String TAG = StrapKitJsInterface.class.getSimpleName();

    /** Instantiate the interface and set the context */
    public StrapKitJsInterface(StrapKitMessageService c) {
        mService = c;

    }

    public void startWebView() {
        mWebView = new WebView(mService);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebView.addJavascriptInterface(this, "strapkit_bridge");
        init();
    }

    //JS Interface methods


    @JavascriptInterface
    public void showPage(String json) {
        try {
            mService.sendMessage("/show", json);
            Log.d(TAG, "show Page: " + json);
        } catch (Exception e) {
            Log.d(TAG, "failed to parse", e);

        }
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
