package com.straphq.strapkit.sensor;

import com.straphq.strapkit.StrapkitBridge;

/**
 * Created by jonahback on 10/9/14.
 */
public interface StrapkitSensor {
    public void startListening();
    public void stopListening();

    public void setStrapkitBridge(StrapkitBridge bridge);

}
