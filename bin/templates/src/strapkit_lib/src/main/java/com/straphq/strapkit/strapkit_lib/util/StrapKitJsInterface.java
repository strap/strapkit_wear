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

    boolean metricsEnabled = false;

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
    public void initMetrics(String app_id) {
        mService.initializeStrapMetrics(app_id);
    }

    @JavascriptInterface
    public void logEvent(String event, String data) {
        JsonObject object = new JsonObject();
        object.addProperty("event", event);
        if (data.equals("undefined")) {
            data = null;
            object.addProperty("data", data);
        } else {
            JsonParser parser = new JsonParser();
            object.add("data", parser.parse(data));
        }
        mService.sendMessage(StrapKitConstants.ACTION_LOG_EVENT, object.toString());
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

        httpClient.processHTTPCall(new HttpClient.HttpClientCallback() {
            @Override
            public void OnHttpComplete(String callback, String info) {
                evaluateJs(callback, info);
            }

        });
    }

    @JavascriptInterface
    public JSONObject getJavaJSONObject(String data) {

        try {
            JSONObject object = new JSONObject(data);

            return object;
        } catch (Exception e) {
            Log.d(TAG, "Failed", e);
            return null;
        }

    }

    public void log(String msg) {
        Log.d("Strapkit", msg);
    }

    public void init() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("file:///android_asset/index.html");
                StrapKitJsInterface.this.loaded = true;
                // String html = "<html><script type=\"text/javascript\" src=\"file:///android_asset/js/lib/klass.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/page.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/view.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/card.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/httpClient.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/list.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/modules/text.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/js/strapkit.js\"></script><script type=\"text/javascript\" src=\"file:///android_asset/app.js\"></script><body></body></html>";
                // mWebView.loadDataWithBaseURL("file:////android_asset/", html, "text/html", "utf-8", "");
                //mWebView.loadUrl("javascript:strapkit.init()");
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
                    String javascriptNoComments = javascript.replaceAll("(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/|[ \\t]*//.*)", "");
                    String javascriptFunction = "javascript:var myMethod = " + javascriptNoComments + ";";
                    if (info != null) {
                        try {
                            String cleanedArgs = info.replace("\\\"", "\"");
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
