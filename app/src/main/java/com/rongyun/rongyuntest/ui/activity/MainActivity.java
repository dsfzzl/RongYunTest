package com.rongyun.rongyuntest.ui.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.rongyun.rongyuntest.R;
import com.rongyun.rongyuntest.presenter.IMainPresenter;
import com.rongyun.rongyuntest.presenter.component.DaggerMainComponent;
import com.rongyun.rongyuntest.presenter.component.MainModule;
import com.rongyun.rongyuntest.server.TestRetrofit;
import com.rongyun.rongyuntest.ui.activity.viewi.MainView;
import com.rongyun.rongyuntest.utils.Base64Utils;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements MainView{

    private Context mContext;
    private TestRetrofit mTestRetrofit;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;
    private final static String TAG = "MainActivity";
    @Inject
    IMainPresenter mMainPresenter;
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mTestRetrofit = new TestRetrofit(this);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE_CODE);
        DaggerMainComponent.builder().mainModule(new MainModule(this))
                .build().inject(this);
        final TextView tv = findViewById(R.id.tv_call);
        mIv = findViewById(R.id.imageView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(mContext,SecondActivity.class);
                //startActivity(intent);

                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .forResult(PictureConfig.CHOOSE_REQUEST);

            }
        });

        findViewById(R.id.tv_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mTestRetrofit.login("86","15329160258","123456",tv);
                //mTestRetrofit.test();
               // WifiHotspotActivity.getIntenst(mContext);
                mTestRetrofit.getToken();
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

                //mTestRetrofit.upImage();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    LocalMedia localMedia = selectList.get(0);
                    //Log.d(TAG,localMedia.getPath());
                    String s = Base64Utils.imageToBase64(localMedia.getPath());
                    Log.d(TAG,s);
                    Bitmap bitmap = BitmapFactory.decodeFile(localMedia.getPath());
                    mIv.setVisibility(View.VISIBLE);
                    mIv.setImageBitmap(bitmap);
                    mTestRetrofit.jianCheLian(TestRetrofit.tokenBean.access_token,s);
                    break;
            }
        }
    }
}
