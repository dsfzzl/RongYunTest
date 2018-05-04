package com.rongyun.rongyuntest.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rongyun.rongyuntest.R;
import com.rongyun.rongyuntest.presenter.IMainPresenter;
import com.rongyun.rongyuntest.presenter.component.DaggerMainComponent;
import com.rongyun.rongyuntest.presenter.component.MainModule;
import com.rongyun.rongyuntest.server.TestRetrofit;
import com.rongyun.rongyuntest.ui.activity.viewi.MainView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView{

    private Context mContext;
    private TestRetrofit mTestRetrofit;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;
    @Inject
    IMainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mTestRetrofit = new TestRetrofit(this);
        DaggerMainComponent.builder().mainModule(new MainModule(this))
                .build().inject(this);
        final TextView tv = findViewById(R.id.tv_call);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,SecondActivity.class);
                startActivity(intent);
                //mTestRetrofit.getNewsList();
                //mTestRetrofit.login("86","15329160258","123456",tv);
                /*if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE_CODE)){
                    mTestRetrofit.upImage();
                }*/
                /*String s = mMainPresenter.get();
                tv.setText(s);*/

          /*      List<String> strings = new ArrayList<String>();
                strings.add("1111");
                strings.add("22222");
                strings.add("33333");
                strings.add("44444");
                strings.add("555555");
                strings.add("666666");

                Observable.just(strings).subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.e("看看参数",strings.toString());
                    }
                });*/
            }
        });

        findViewById(R.id.tv_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mTestRetrofit.login("86","15329160258","123456",tv);
                //mTestRetrofit.test();
                WifiHotspotActivity.getIntenst(mContext);
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
                mTestRetrofit.upImage();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
