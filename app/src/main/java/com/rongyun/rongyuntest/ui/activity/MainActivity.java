package com.rongyun.rongyuntest.ui.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rongyun.rongyuntest.R;
import com.rongyun.rongyuntest.server.TestRetrofit;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private TestRetrofit mTestRetrofit;

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
}
