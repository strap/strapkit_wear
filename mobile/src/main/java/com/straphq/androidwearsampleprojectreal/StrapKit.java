package com.straphq.androidwearsampleprojectreal;

import android.webkit.JavascriptInterface;
import android.content.Context;
import android.webkit.WebView;
import android.util.Log;

import com.google.android.gms.wearable.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.common.api.PendingResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
/**
 * Created by jonahback on 9/19/14.
 */
public class StrapKit implements DataApi.DataListener {
    Context mContext;
    GoogleApiClient mGoogleApiClient;
    WebView mWebView;

    /** Instantiate the interface and set the context */
    StrapKit(Context c, GoogleApiClient mGoogleApiClient) {
        mContext = c;
        this.mGoogleApiClient = mGoogleApiClient;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void setView(String viewText, String viewID) {
        try {
            JSONObject viewJSON = new JSONObject();
            System.out.print(viewID);

            PutDataMapRequest dataMap = PutDataMapRequest.create("/views/" + viewID);
            dataMap.getDataMap().putString("title", viewText);
            dataMap.getDataMap().putString("date", new Date().toString());
            PutDataRequest request = dataMap.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                    .putDataItem(mGoogleApiClient, request);
        } catch (Exception e) {

        }


    }

    public void log(String msg) {
        Log.d("Strapkit", msg);
    }
    @Override
    public void onDataChanged(DataEventBuffer buffer) {
        for(DataEvent evt : buffer) {
            if(evt.getDataItem().getUri().getPathSegments().get(1).matches("onTouch")) {
                mWebView.loadUrl("javascript:strapkit.onTouch(1, 'blahdata')");
            }
        }
    }
}