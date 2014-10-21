package com.straphq.strapkit;

import android.webkit.JavascriptInterface;
import android.content.Context;
import android.webkit.WebView;
import android.util.Log;
import android.os.Handler;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.common.api.PendingResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Date;
import java.util.ArrayList;

import com.straphq.strapkit_lib.sensor.StrapkitSensorData;
import com.straphq.strapkit_lib.ui.StrapkitListView;
import com.straphq.strapkit_lib.ui.StrapkitView;
import com.straphq.wear_sdk.StrapMetrics;

/**
 * Created by jonahback on 9/19/14.
 */
public class StrapKit implements DataApi.DataListener, MessageApi.MessageListener {
    Context mContext;
    GoogleApiClient mGoogleApiClient;
    WebView mWebView;
    private Handler handler = new Handler();
    private StrapMetrics metrics;
    private Node mNode;

    boolean metricsEnabled = false;


    /** Instantiate the interface and set the context */
    StrapKit(Context c, GoogleApiClient mGoogleApiClient) {
        mContext = c;
        this.mGoogleApiClient = mGoogleApiClient;
        metrics = new StrapMetrics();

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback( new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                for (Node n : result.getNodes()) {
                    mNode = n;
                }
            }
        });
    }

  //JS Interface methods


    //StrapMetrics methods
    @JavascriptInterface
    public void strapMetricsInit(String appID) {
        metrics.initFromPhone(appID);

        metricsEnabled = true;
    }

    @JavascriptInterface
    public void strapMetricsLogEvent(String event, String cvar) {
        JSONObject cvarData = null;
        try {
            cvarData = new JSONObject(cvar);
        } catch (JSONException e) {

        }
        metrics.logEvent(event, cvarData);
    }

    @JavascriptInterface
    public void setListView(String title, String viewID, String listJSON) {
        JSONArray listItems = null;
        ArrayList<String> listStrings = new ArrayList<String>();
        try {
            listItems = new JSONArray(listJSON);
        } catch (JSONException e) {

        }

        if(listItems != null) {

            for(int i=0; i<listItems.length(); i++) {
                try{
                    JSONObject data = listItems.getJSONObject(i);
                    listStrings.add(data.getString("title"));
                 } catch (JSONException e) {

                }
            }

            StrapkitListView listView = new StrapkitListView(viewID);
            listView.setType(2);
            listView.setListItems(listStrings);

            serializeAndSend(listView, "view");
        }
    }

    @JavascriptInterface
    public void setTextView(String viewText, String viewID) {
        try {
            JSONObject viewJSON = new JSONObject();

            StrapkitView view = new StrapkitView(viewID);
            view.setType(1);
            view.setTitle(viewText);
            view.setChild(null);

            serializeAndSend(view, "view");
        } catch (Exception e) {

        }

    }

    private void serializeAndSend(Object rawObjectToSend, String path) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[]  binaryData = null;
        try {
            out = new ObjectOutputStream(byteArrayOutputStream);
            out.writeObject(rawObjectToSend);
            binaryData = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {

        }
        Wearable.MessageApi.sendMessage(mGoogleApiClient,mNode.getId(),path,  binaryData);
    }

    //Sensor methods

    //Android-specific methods
    @JavascriptInterface
    public void  confirmActivity(String message) {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/confirmActivity");
        dataMapRequest.getDataMap().putString("message", message);
        dataMapRequest.getDataMap().putString("date", new Date().toString());

        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(mGoogleApiClient, request);
    }

    public void log(String msg) {
        Log.d("Strapkit", msg);
    }

    public void init() {
        handler.post(new Runnable() {
            @Override
            public void run() {

                String html = "<html><script src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js\"></script><script src=\"file:///android_asset/ajax.js\"></script><script src=\"file:///android_asset/strapkit.js\"></script><script src=\"file:///android_asset/app.js\"></script><body></body></html>";
                mWebView.loadDataWithBaseURL("file:////android_asset/", html, "text/html", "utf-8", "");
                //mWebView.loadUrl("javascript:strapkit.init()");
            }
        });
    }

    //Wear methods
    public void onTouch(final String viewID) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:strapkit.onTouch('" + viewID + "', 'blahdata')");
            }
        });
    }

    public void onSelect(final String viewID, final int position) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:strapkit.onSelect('" + viewID + "'," + position + ")");
            }
        });
    }

    public void injectSensorData(final StrapkitSensorData data) {
        final String x = data.asJson().toString();
        if(mGoogleApiClient.isConnected()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String y = "javascript:strapkit.handleSensor(" + data.asJson().toString() + ")";
                    mWebView.loadUrl(y);
                }
            });
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer buffer) {
        for(DataEvent evt : buffer) {

            if(metricsEnabled && metrics.canHandleMsg(evt)) {
                try {
                    metrics.processReceiveData(DataMapItem.fromDataItem(evt.getDataItem()).getDataMap());

                } catch (Exception e) {

                }
            }

            if(evt.getDataItem().getUri().getPathSegments().get(0).equals("strapkitInit")) {
                init();
            }

            if(evt.getDataItem().getUri().getPathSegments().get(0).equals("onTouch")) {
                DataItem item = evt.getDataItem();
                DataMap map = DataMapItem.fromDataItem(item).getDataMap();
                final String viewID = map.getString("id");
                onTouch(viewID);
            }

            if(evt.getDataItem().getUri().getPathSegments().get(0).equals("onSelect")) {
                DataItem item = evt.getDataItem();
                DataMap map = DataMapItem.fromDataItem(item).getDataMap();
                final String viewID = map.getString("id");
                final int position = map.getInt("position");
                onSelect(viewID, position);
            }
        }
        buffer.release();
    }


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if(messageEvent.getPath().equals("sensor")) {
            StrapkitSensorData data = null;
            try {
                InputStream inputStream = new ByteArrayInputStream(messageEvent.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                data = (StrapkitSensorData) objectInputStream.readObject();
                injectSensorData(data);

            } catch (IOException e) {

            } catch (ClassNotFoundException e) {

            }

        }
    }
}