package com.straphq.strapkit_lib.sensor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by jonahback on 10/17/14.
 */
public class StrapkitSensorData implements Serializable {

    public float[] values;
    public float accuracy;
    public int sensor_type;

    public StrapkitSensorData(float[] vals, float acc, int sensor_type) {
        values = vals;
        accuracy = acc;
        this.sensor_type = sensor_type;
    }

    public JSONObject asJson() {
        JSONObject obj = new JSONObject();
        JSONArray vals = null;
        try {
            vals = new JSONArray(values);
            obj.put("values", vals);
            obj.put("accuracy", accuracy);
        } catch (JSONException e) {

        }
        return obj;
    }
}
