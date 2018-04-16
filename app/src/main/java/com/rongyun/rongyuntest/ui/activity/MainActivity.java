package com.rongyun.rongyuntest.ui.activity;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rongyun.rongyuntest.R;
import com.rongyun.rongyuntest.server.TestRetrofit;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private TestRetrofit mTestRetrofit;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mTestRetrofit = new TestRetrofit();

        final TextView tv = findViewById(R.id.tv_call);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTestRetrofit.login("86","15329160258","123456",tv);
               /* if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE_CODE)){
                    mTestRetrofit.downLoad(mContext);
                }*/
            }
        });


       /* String token = "hanV86LvMsen9K5G6kXQnXWktRiU98NxTY/fCyOrNZxU+U5tbr+wE5VpltloH7+sAqp5rvbcWmizkBH7s6HgcQ==";
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e("MainActivity","onTokenIncorrect");
            }

            @Override
            public void onSuccess(String userid) {
                Log.e("MainActivity","onSuccess : " + userid);
                //startActivity(new Intent(mContext,ConversationListActivity.class));
               // startActivity(new Intent(mContext,ConversationActivity.class));
                RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.PRIVATE,13+"","hhhh");
                //RongIM.getInstance().startSubConversationList(mContext,Conversation.ConversationType.NONE);



            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("MainActivity","onError : " + errorCode);
            }
        });*/
    }


    private boolean checkPermission(String permission,int requestCode) {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mContext,permission)) {
            ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                mTestRetrofit.downLoad(mContext);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
