package com.straphq.strapkit.framework;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.straphq.strapkit.strapkit_lib.messaging.StrapKitMessageService;
import com.straphq.strapkit.strapkit_lib.util.StrapKitJsInterface;

import java.io.InputStream;


public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Intent serviceIntent = new Intent(this, StrapKitMessageService.class);
        startService(serviceIntent);
    }
}
