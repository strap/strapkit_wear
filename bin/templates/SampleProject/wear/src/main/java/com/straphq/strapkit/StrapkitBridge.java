package com.straphq.strapkit;

import android.hardware.SensorManager;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.straphq.strapkit.sensor.StrapkitSensor;
import com.straphq.strapkit_lib.sensor.StrapkitSensorData;
import com.straphq.strapkit_lib.ui.StrapkitListView;
import com.straphq.strapkit_lib.ui.StrapkitView;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * Created by jonahback on 9/29/14.
 */
public class StrapkitBridge implements DataApi.DataListener, MessageApi.MessageListener {

    private GoogleApiClient mGoogleApiClient;
    private StrapkitActivity activity;
    private SensorManager sensorManager;
    private Context context;
    private Node mNode;

    public StrapkitBridge() {

    }

    public StrapkitBridge(GoogleApiClient apiClient, StrapkitActivity activity, Context context) {
        mGoogleApiClient = apiClient;
        this.activity = activity;
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mGoogleApiClient.connect();
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        initializeView();

    }

    private void initializeView() {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/strapkitInit");
        dataMapRequest.getDataMap().putString("date", new Date().toString());

        PutDataRequest request = dataMapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(mGoogleApiClient, request);
        return;
    }

    public void onTouch(String viewID) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/onTouch");
        dataMap.getDataMap().putString("id", viewID);
        dataMap.getDataMap().putString("date", new Date().toString());
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(mGoogleApiClient, request);

        return;
    }

    public void onSelect(String viewID, int position) {

        PutDataMapRequest dataMap = PutDataMapRequest.create("/onSelect");
        dataMap.getDataMap().putString("id", viewID);
        dataMap.getDataMap().putInt("position", position);
        dataMap.getDataMap().putString("date", new Date().toString());
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(mGoogleApiClient, request);

        return;
    }

    public void sendSensorData(final StrapkitSensorData sensorData) {

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback( new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                for(Node n : result.getNodes()) {
                    mNode = n;
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream out = null;
                byte[]  binaryData = null;
                try {
                    out = new ObjectOutputStream(byteArrayOutputStream);
                    out.writeObject(sensorData);
                    binaryData = byteArrayOutputStream.toByteArray();
                } catch (IOException e) {

                }
                Wearable.MessageApi.sendMessage(mGoogleApiClient,mNode.getId(),"sensor",  binaryData);
            }
        });


    }



    public void onDataChanged(DataEventBuffer buffer) {
        for (DataEvent event : buffer) {
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            DataMap map = dataMapItem.getDataMap();

            if(event.getDataItem().getUri().getPathSegments().get(0).equals("confirmActivity")) {
                activity.confirmActivity(map.getString("message"));
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Object data;
        try {
            InputStream inputStream = new ByteArrayInputStream(messageEvent.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            data = objectInputStream.readObject();

            if(messageEvent.getPath().equals("view")) {
                StrapkitView view = (StrapkitView) data;
                activity.updateView(view);
            }


        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
    }
}
