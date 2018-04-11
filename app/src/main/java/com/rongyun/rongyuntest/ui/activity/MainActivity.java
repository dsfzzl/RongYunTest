package com.rongyun.rongyuntest.ui.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rongyun.rongyuntest.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        String token = "hanV86LvMsen9K5G6kXQnXWktRiU98NxTY/fCyOrNZxU+U5tbr+wE5VpltloH7+sAqp5rvbcWmizkBH7s6HgcQ==";
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
                RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.CHATROOM,userid,"hhhh");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("MainActivity","onError : " + errorCode);
            }
        });
    }
}
