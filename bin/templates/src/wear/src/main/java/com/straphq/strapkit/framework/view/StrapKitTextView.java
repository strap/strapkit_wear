package com.straphq.strapkit.framework.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.straphq.strapkit.framework.R;
import com.straphq.strapkit.framework.StrapKitBaseActivity;

/**
 * Created by martinza on 1/6/15.
 */
public class StrapKitTextView extends StrapKitBaseView {

    private static final String TAG = StrapKitTextView.class.getSimpleName();

    private String text;
    private String position;


    private static final String POSITION_CENTER = "center";
    private static final String POSITION_RIGHT = "right";
    private static final String POSITION_LEFT = "left";

    private void setPosition(TextView textView) {
        switch (position) {
            case POSITION_CENTER:
                textView.setGravity(Gravity.CENTER);
                break;
            case POSITION_RIGHT:
                textView.setGravity(Gravity.RIGHT);
                break;
            default:
                break;
        }
    }

    @Override
    public void initialize(StrapKitBaseActivity activity) {
        TextView mTextView = null;
        if (mTextView == null) {
            mTextView = new TextView(activity);
            mTextView.setText(text);
            setPosition(mTextView);
            ViewGroup viewGroup = (ViewGroup) activity.findViewById(R.id.frame_layout);
            viewGroup.addView(mTextView);
        }
    }
}
