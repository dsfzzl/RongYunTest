package com.rongyun.rongyuntest.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.rongyun.rongyuntest.R;

/**
 * Created by Administrator on 2018/5/11.
 */

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private SurfaceView mSurfaceView;
    private Button mBntStrat;
    private Button mStop;
    private Button mPlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mSurfaceView = findViewById(R.id.sf_view);
        mBntStrat = findViewById(R.id.start);
        mStop = findViewById(R.id.stop);
        mPlay = findViewById(R.id.play);
        mBntStrat.setOnClickListener(this);
        mStop.setOnClickListener(this);
        mPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:

                break;
            case R.id.stop:
                break;
            case R.id.play:
                break;
        }
    }
}
