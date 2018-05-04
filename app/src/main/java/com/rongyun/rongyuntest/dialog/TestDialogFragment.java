package com.rongyun.rongyuntest.dialog;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rongyun.rongyuntest.R;
import com.rongyun.rongyuntest.ui.activity.WifiHotspotActivity;

/**
 * Created by Administrator on 2018/5/2.
 *
 */

public class TestDialogFragment extends DialogFragment {

    private EditText mEtPwd;
    private Button mBntSure;
    private TextView mTvNmae;
    private WifiHotspotActivity mListener;

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimator(transit, enter, nextAnim);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (WifiHotspotActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dailog_input_pwd, null);
        mTvNmae = view.findViewById(R.id.tv_name);
        mEtPwd = view.findViewById(R.id.et_pwd);
        mBntSure = view.findViewById(R.id.bnt_sure);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).
                setView(view).create();
        mBntSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = mEtPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    return;
                }
                dismiss();
                mListener.conntWifi(pwd);
            }
        });
        return alertDialog;
    }


    public void show(FragmentManager manager, String tag,String name) {
        //mTvNmae.setText(name);
        super.show(manager, tag);
    }
}
