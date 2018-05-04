package com.rongyun.rongyuntest.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rongyun.rongyuntest.R;
import com.rongyun.rongyuntest.dialog.TestDialogFragment;
import com.rongyun.rongyuntest.utils.WifiAdmin;

import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 */

public class WifiHotspotActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "WifiHotspotActivity";
    private WifiAdmin mWifiAdmin;
    private TextView mTvOpen;
    private TextView mTvState;
    private TextView mTvCreate;
    private ListView mLvList;
    private List<ScanResult> mScanResultes;
    private Context mContext ;
    private WifiListAdapter mAdapter;
    private TestDialogFragment mTestDialogFragment;
    private int mWifiType;
    private ScanResult mScanResult;


    public static void getIntenst(Context context) {
        context.startActivity(new Intent(context,WifiHotspotActivity.class));
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_hotspot);
        mContext = this;
        mWifiAdmin = WifiAdmin.getInstance(this);
        mWifiAdmin.registerReceiver(this,wifiListener);
        mTvOpen = findViewById(R.id.open);
        mTvState = findViewById(R.id.state);
        mTvCreate = findViewById(R.id.create);
        mLvList = findViewById(R.id.lv_list);
        findViewById(R.id.scan).setOnClickListener(this);
        mAdapter = new WifiListAdapter();
        mLvList.setAdapter(mAdapter);
        mTvState.setText(mWifiAdmin.isWifiEnabled()?"已经打开":"没有打开");
        mTvOpen.setOnClickListener(this);
        mTvCreate.setOnClickListener(this);
        mTvState.setOnClickListener(this);
        mTestDialogFragment = new TestDialogFragment();

        mLvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= mScanResultes.size())return;
                WifiInfo wifiInfo = mWifiAdmin.getWifiInfo();
                mScanResult = mScanResultes.get(position);
                if (wifiInfo != null && wifiInfo.getSSID().equals("\""+ mScanResult.SSID +"\"")) {
                    //就是当前连接的wifi
                    return;
                }
                mWifiType = mWifiAdmin.getWifiType(mScanResult);
                if (mWifiType != WifiAdmin.WIFICIPHER_NOPASS) {
                    //查看网络是否已经连接过
                    WifiConfiguration wifiConfiguration = mWifiAdmin.isExsits(mScanResult.SSID);
                    if (wifiConfiguration != null) {
                        Log.d(TAG,"已有连接配置");
                        //已经连接过
                        mWifiAdmin.addNetwork(wifiConfiguration);
                    }else {
                        mTestDialogFragment.show(getFragmentManager(),"111",mScanResult.SSID);
                    }
                }else { //这里先判断连接类型，不需要密码的每次都要重新创建连接信息，不需要密码的wifi,删除网络后还是能从连接配置中获取到配置信息，但是使用这个配置信息，不能连接到wifi
                    Log.d(TAG,"没有密码的连接");
                    WifiConfiguration  wifiConfiguration = mWifiAdmin.createWifiInfo(mScanResult.SSID, "", mWifiType, "wt");
                    mWifiAdmin.addNetwork(wifiConfiguration);
                }


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open:
                if (!mWifiAdmin.isWifiEnabled()){
                    mWifiAdmin.OpenWifi();
                    mTvOpen.setText("关闭");
                }else {
                    mWifiAdmin.closeWifi();
                    mTvOpen.setText("打开");
                }
                break;
            case R.id.create:
                mWifiAdmin.openHots(mWifiAdmin.createWifiInfo("我是热点","123456",WifiAdmin.WIFICIPHER_WEP,"ap"));
                break;
            case R.id.state:
                //ArrayList<String> connectedIP = mWifiAdmin.getConnectedIP();
                break;
            case R.id.scan:
                mWifiAdmin.startScan();
                break;
            default:
                break;
        }

    }

    public void conntWifi(String pwd){
        WifiConfiguration wifiConfiguration = mWifiAdmin.createWifiInfo(mScanResult.SSID, pwd, mWifiType, "wt");
        mWifiAdmin.addNetwork(wifiConfiguration);
    }


    private WifiAdmin.WifiStateChangeListener wifiListener = new WifiAdmin.WifiStateChangeListener() {
        @Override
        public void connSuccess(WifiInfo wifiInfo) {
            mTvState.setText("已连接：" + wifiInfo.getSSID() +"\nmac地址："+ wifiInfo.getMacAddress());
        }

        @Override
        public void scanSuccess(List<ScanResult> scanResults) {
            mScanResultes = scanResults;
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void connState(NetworkInfo.DetailedState state, String des) {
            mTvState.setText(des);
        }

        @Override
        public void disConn() {
            mTvState.setText("wifi已断开");
        }
    };

    class WifiListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mScanResultes != null?mScanResultes.size():0;
        }

        @Override
        public Object getItem(int position) {
            if (mScanResultes != null && position < mScanResultes.size())
                return mScanResultes.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodler hodler = null;
            if (convertView != null) {
                hodler = (ViewHodler) convertView.getTag();
            }else {
                hodler = new ViewHodler();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wifi_list,null);
                hodler.mTextView = convertView.findViewById(R.id.tv_info);
                convertView.setTag(hodler);
            }
            ScanResult scanResult = mScanResultes.get(position);
            int level = scanResult.level;
            if (level <=0 && level >= -50) {
                hodler.mTextView.setText("扫描到的wifi: "+scanResult.SSID + "  信号很好");
            }else if (level >= -70) {
                hodler.mTextView.setText("扫描到的wifi: "+scanResult.SSID + "  信号较好");
            }else if (level >= -80) {
                hodler.mTextView.setText("扫描到的wifi: "+scanResult.SSID + "  信号一般");
            }else if (level >=-100){
                hodler.mTextView.setText("扫描到的wifi: "+scanResult.SSID + "  信号较差");
            }else {
                hodler.mTextView.setText("扫描到的wifi: "+scanResult.SSID + "  信号很差");
            }
            return convertView;
        }
    }

    static class ViewHodler {
        public TextView mTextView;

    }



    @Override
    protected void onDestroy() {
        mWifiAdmin.unRegisterReceiver(this);
        super.onDestroy();
    }
}
