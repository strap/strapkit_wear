package com.straphq.strapkit.framework;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.straphq.strapkit.strapkit_lib.util.StrapKitJsInterface;

import java.io.InputStream;


public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        StrapKitJsInterface strapKitJsInterface = new StrapKitJsInterface(this);
        strapKitJsInterface.startWebView();
    }
}
