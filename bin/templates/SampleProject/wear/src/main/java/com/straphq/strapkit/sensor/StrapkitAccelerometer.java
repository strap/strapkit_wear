package com.straphq.strapkit.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.straphq.strapkit.StrapkitBridge;
import com.straphq.strapkit_lib.sensor.StrapkitSensorData;

/**
 * Created by jonahback on 10/17/14.
 */
public class StrapkitAccelerometer implements SensorEventListener, StrapkitSensor {
    private Context mContext = null;
    private SensorManager mSensorManager = null;
    private Sensor mSensor = null;
    private StrapkitBridge mBridge = null;

    public StrapkitAccelerometer(Context context) {
        mContext = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        StrapkitSensorData sensorData = new StrapkitSensorData(event.values, event.accuracy, 1);
        mBridge.sendSensorData(sensorData);
    }

    @Override
    public void startListening() {
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void stopListening() {
        mSensorManager.unregisterListener(this);
    }

    public void setStrapkitBridge(StrapkitBridge bridge) {
        mBridge = bridge;
    }
}
