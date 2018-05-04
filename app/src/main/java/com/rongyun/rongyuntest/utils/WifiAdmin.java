package com.rongyun.rongyuntest.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.IntDef;
import android.support.compat.BuildConfig;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 */

public class WifiAdmin {
    private static final String TAG = "WifiAdmin";
    private static  WifiAdmin wifiAdmin = null;
    public static final int WIFICIPHER_WPA = 1;
    public static final int WIFICIPHER_WEP = 2;
    public static final int WIFICIPHER_NOPASS = 3;

    private List<WifiConfiguration> mWifiConfiguration; //无线网络配置信息类集合(网络连接列表)
    private List<ScanResult> mWifiList; //检测到接入点信息类 集合

    //描述任何Wifi连接状态
    private WifiInfo mWifiInfo;

    WifiManager.WifiLock mWifilock; //能够阻止wifi进入睡眠状态，使wifi一直处于活跃状态
    public WifiManager mWifiManager;
    @IntDef({WIFICIPHER_WPA,WIFICIPHER_WEP,WIFICIPHER_NOPASS})
    @Retention(RetentionPolicy.SOURCE)  //RetentionPolicy.SOURCE 的注解类型的生命周期只存在Java源文件这一阶段，是3种生命周期中最短的注解
    public @interface wifiType {}
    /**
     * 获取该类的实例（懒汉）
     * @param context
     * @return
     */
    public static WifiAdmin getInstance(Context context) {
        if(wifiAdmin == null) {
            synchronized (WifiAdmin.class) {
                if (wifiAdmin == null) {
                    wifiAdmin = new WifiAdmin(context);
                }
            }
        }
        return wifiAdmin;
    }
    private WifiAdmin(Context context) {
        //获取系统Wifi服务   WIFI_SERVICE
        this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 是否存在网络信息
     * @param str  热点名称
     * @return
     */
    public WifiConfiguration isExsits(String str) {
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration configuration :configuredNetworks){
            if (configuration.SSID.equals("\"" + str + "\"")) {

                return configuration;
            }
        }
        return null;
    }

    /**锁定WifiLock，当下载大文件时需要锁定 **/
    public void AcquireWifiLock() {
        this.mWifilock.acquire();
    }
    /**创建一个WifiLock**/
    public void CreateWifiLock() {
        this.mWifilock = this.mWifiManager.createWifiLock("Test");
    }
    /**解锁WifiLock**/
    public void ReleaseWifilock() {
        if(mWifilock.isHeld()) { //判断时候锁定
            mWifilock.acquire();
        }
    }

    /**
     * d当前wifi是否打开
     * @return
     */
    public boolean isWifiEnabled(){
        return mWifiManager.isWifiEnabled();
    }


    /**打开Wifi**/
    public void OpenWifi() {
        if(!this.mWifiManager.isWifiEnabled()){ //当前wifi不可用
            this.mWifiManager.setWifiEnabled(true);
        }
    }
    /**关闭Wifi**/
    public void closeWifi() {
        if(mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }
    /**端口指定id的wifi**/
    public void disconnectWifi(int paramInt) {
        this.mWifiManager.disableNetwork(paramInt);
    }

    /**
     * 断开当前连接
     */
    public void disConntect() {
        mWifiManager.disconnect();
    }

    /**添加指定网络**/
    public void addNetwork(WifiConfiguration paramWifiConfiguration) {
        disConntect();  //在连接新的wifi之前断开当前wifi
        int i = mWifiManager.addNetwork(paramWifiConfiguration);
        mWifiManager.enableNetwork(i, true);
    }

    public @wifiType int getWifiType(ScanResult scanResult) {

        String capabilities = scanResult.capabilities;
        if (TextUtils.isEmpty(capabilities)) {
            if (BuildConfig.DEBUG)
                throw new NullPointerException("capabilities 为空");
        }else {
            if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                return WIFICIPHER_WPA;
            } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
               return WIFICIPHER_WEP;
            } else {
                return WIFICIPHER_NOPASS;
            }
        }
        return WIFICIPHER_NOPASS;
    }

    /**
     * 连接指定配置好的网络
     * @param index 配置好网络的ID
     */
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        //连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
    }

    /**
     * 根据wifi信息创建或关闭一个热点
     * @param paramWifiConfiguration
     * @param paramBoolean 关闭标志
     */
    private void createWifiAP(WifiConfiguration paramWifiConfiguration,boolean paramBoolean) {
        if (mWifiManager.isWifiEnabled()) {
            //如果wifi处于开启状态，关闭wifi
            mWifiManager.setWifiEnabled(false);
        }
        String state = paramBoolean ? "打开":"关闭";
        try {  //
            Class localClass = this.mWifiManager.getClass();
            Method method = localClass.getMethod( "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            boolean enable = (boolean) method.invoke(mWifiManager, paramWifiConfiguration, paramBoolean);
            if (enable) {
                Log.d(TAG,"wifi热点"+state+"成功");
            }else {
                Log.e(TAG,"wifi热点"+state+"失败");
            }
            return;
        } catch (Exception e) {
            Log.e(TAG,"wifi热点"+state+"失败");
            e.printStackTrace();
        }
    }

    /**
     * 打开wifi 热点
     * @param paramWifiConfiguration
     */
    public void openHots(WifiConfiguration paramWifiConfiguration){
        if (!isWifiHotsEnabled()) {
            createWifiAP(paramWifiConfiguration,true);
        }else {
            Log.d(TAG,"wifi热点已经打开，不需要再打开");
        }
    }
    /**
     * 关闭wifi热点
     */
    public void closeHots(){
        if (isWifiHotsEnabled()) {
            try {
                Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(mWifiManager);
                createWifiAP(config,false);
            } catch (Exception e) {
                Log.e(TAG,"wifi热点关闭失败");
                e.printStackTrace();
            }
        }else {
            Log.d(TAG,"wifi热点已经关闭");
        }

    }

    /**
     * wifi热点是否已经打开
     * @return
     */
    public boolean isWifiHotsEnabled(){
        try {
            Method method=mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(mWifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开热点手机获得其他连接手机IP的方法
     * @return 其他手机IP 数组列表
     */
    public ArrayList<String> getConnectedIP(){
        ArrayList<String> connectedIp=new ArrayList<String>();
        try {
            BufferedReader br=new BufferedReader(new FileReader(
                    "/proc/net/arp"));
            String line;
            while ((line=br.readLine())!=null){
                Log.d(TAG,"连接的的手机的热点信息"+line);
                String[] splitted=line.split(" +");
                if (splitted !=null && splitted.length>=4){
                    String ip=splitted[0];
                    if (!ip.equalsIgnoreCase("ip")){
                        connectedIp.add(ip);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectedIp;
    }

    /**
     * 创建一个wifi信息
     * @param ssid 名称
     * @param passawrd 密码
     * @param wifiType
     * @param type 是"ap"还是"wifi"
     * @return
     */
    public WifiConfiguration createWifiInfo(String ssid, String passawrd,@wifiType int wifiType, String type) {
        //配置网络信息类
        WifiConfiguration localWifiConfiguration1 = new WifiConfiguration();
        //设置配置网络属性
        localWifiConfiguration1.allowedAuthAlgorithms.clear();
        localWifiConfiguration1.allowedGroupCiphers.clear();
        localWifiConfiguration1.allowedKeyManagement.clear();
        localWifiConfiguration1.allowedPairwiseCiphers.clear();
        localWifiConfiguration1.allowedProtocols.clear();

        if(type.equals("wt")) { //wifi连接
           /* if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            }else {
                localWifiConfiguration1.SSID = ("\"" + ssid + "\"");
            }*/
            localWifiConfiguration1.SSID = ("\"" + ssid + "\"");
            WifiConfiguration localWifiConfiguration2 = isExsits(ssid);
            if(localWifiConfiguration2 != null) {
                mWifiManager.removeNetwork(localWifiConfiguration2.networkId); //从列表中删除指定的网络配置网络
            }
            if(wifiType == WIFICIPHER_NOPASS) { //没有密码
                localWifiConfiguration1.wepKeys[0] = "\"" + "\"";
                localWifiConfiguration1.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                localWifiConfiguration1.wepTxKeyIndex = 0;
            } else if(wifiType == WIFICIPHER_WEP) { //简单密码
                localWifiConfiguration1.hiddenSSID = true;
               /* if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    localWifiConfiguration1.wepKeys[0] = passawrd;
                }else {
                    localWifiConfiguration1.wepKeys[0] = ("\"" + passawrd + "\"");
                }*/
                localWifiConfiguration1.wepKeys[0] = ("\"" + passawrd + "\"");

            } else { //wap加密
               /* if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    localWifiConfiguration1.preSharedKey = passawrd ;
                }else {
                    localWifiConfiguration1.preSharedKey = ("\"" + passawrd + "\"");
                }*/
                localWifiConfiguration1.preSharedKey = ("\"" + passawrd + "\"");
                localWifiConfiguration1.hiddenSSID = true;
                localWifiConfiguration1.allowedAuthAlgorithms.set(0);
                localWifiConfiguration1.allowedGroupCiphers.set(2);
                localWifiConfiguration1.allowedKeyManagement.set(1);
                localWifiConfiguration1.allowedPairwiseCiphers.set(1);
                localWifiConfiguration1.allowedGroupCiphers.set(3);
                localWifiConfiguration1.allowedPairwiseCiphers.set(2);
            }
        }else {//"ap" wifi热点
            localWifiConfiguration1.SSID = ssid;
            localWifiConfiguration1.allowedAuthAlgorithms.set(1);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            localWifiConfiguration1.allowedKeyManagement.set(0);
            localWifiConfiguration1.wepTxKeyIndex = 0;
            if (wifiType == WIFICIPHER_NOPASS) {  //没有密码
                localWifiConfiguration1.wepKeys[0] = "";
                localWifiConfiguration1.allowedKeyManagement.set(0);
                localWifiConfiguration1.wepTxKeyIndex = 0;
            } else if (wifiType == WIFICIPHER_WEP) { //简单密码
                localWifiConfiguration1.hiddenSSID = true;//网络上不广播ssid
                localWifiConfiguration1.wepKeys[0] = passawrd;
            } else if (wifiType == WIFICIPHER_WPA) {//wap加密
                localWifiConfiguration1.preSharedKey = passawrd;
                localWifiConfiguration1.allowedAuthAlgorithms.set(0);
                localWifiConfiguration1.allowedProtocols.set(1);
                localWifiConfiguration1.allowedProtocols.set(0);
                localWifiConfiguration1.allowedKeyManagement.set(1);
                localWifiConfiguration1.allowedPairwiseCiphers.set(2);
                localWifiConfiguration1.allowedPairwiseCiphers.set(1);
            }
        }
        return localWifiConfiguration1;
    }

    /**获取热点名**/
    public String getApSSID() {
        try {
            Method localMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
            if (localMethod == null) return null;
            Object localObject1 = localMethod.invoke(this.mWifiManager,new Object[0]);
            if (localObject1 == null) return null;
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
            if (localWifiConfiguration.SSID != null) return localWifiConfiguration.SSID;
            Field localField1 = WifiConfiguration.class .getDeclaredField("mWifiApProfile");
            if (localField1 == null) return null;
            localField1.setAccessible(true);
            Object localObject2 = localField1.get(localWifiConfiguration);
            localField1.setAccessible(false);
            if (localObject2 == null)  return null;
            Field localField2 = localObject2.getClass().getDeclaredField("SSID");
            localField2.setAccessible(true);
            Object localObject3 = localField2.get(localObject2);
            if (localObject3 == null) return null;
            localField2.setAccessible(false);
            String str = (String) localObject3;
            return str;
        } catch (Exception localException) {
        }
        return null;
    }

    /**获取wifi名**/
    public String getBSSID() {
        if (this.mWifiInfo == null)
            return "NULL";
        return this.mWifiInfo.getBSSID();
    }



    /**获取热点创建状态**/
    public int getWifiApState() {
        try {
            int i = ((Integer) this.mWifiManager.getClass()
                    .getMethod("getWifiApState", new Class[0])
                    .invoke(this.mWifiManager, new Object[0])).intValue();
            return i;
        } catch (Exception localException) {
        }
        return 4;   //未知wifi网卡状态
    }
    /**获取已连接wifi的信息**/
    public WifiInfo getWifiInfo() {
        return this.mWifiManager.getConnectionInfo();
    }
    /** 得到网络列表**/
    public List<ScanResult> getWifiList() {
        return this.mWifiList;
    }

    /**查看扫描结果**/
    public StringBuilder lookUpScan() {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++)
        {
            localStringBuilder.append("Index_"+new Integer(i + 1).toString() + ":");
            //将ScanResult信息转换成一个字符串包
            //其中把包括：BSSID、SSID、capabilities、frequency、level
            localStringBuilder.append((mWifiList.get(i)).toString());
            localStringBuilder.append("\n");
        }
        return localStringBuilder;
    }

    /** 设置wifi搜索结果 **/
    public void setWifiList() {
        this.mWifiList = this.mWifiManager.getScanResults();
    }
    /**开始搜索wifi**/
    public void startScan() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
        this.mWifiManager.startScan();
    }
    /**得到接入点的BSSID**/
    public String GetBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }


     class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.endsWith(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi 状态变化

                Log.d(TAG,"wifi 状态变化 ");
            }else if (action.endsWith(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {//热点扫描结果通知广播
                List<ScanResult> scanResults = mWifiManager.getScanResults();
                if (wifiListener != null){
                    wifiListener.scanSuccess(scanResults);
                }
                Log.d(TAG,"扫描的结果： "+scanResults.toString());
            }else if (action.endsWith(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {//热点连接结果通知广播
                Log.d(TAG,"热点连接结果： ");
            }else if (action.endsWith(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//网络状态变化广播（与上一广播协同完成连接过程通知）
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info== null) return;
                if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {//连接已断开
                    if (wifiListener != null) {
                        wifiListener.disConn();
                    }
                    Log.d(TAG,"连接已断开");
                } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {  //已连接到网络
                    //WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    final WifiInfo wifiInfo = getWifiInfo();
                    if (wifiInfo != null) {
                        if (wifiListener != null) {
                            wifiListener.connSuccess(wifiInfo);
                        }
                        Log.d(TAG,"已连接到网络 : " + wifiInfo.getMacAddress() + "   -" +wifiInfo.getNetworkId());
                    }
                } else {
                    NetworkInfo.DetailedState state = info.getDetailedState();
                    if (state == state.CONNECTING) {
                        Log.d(TAG,"连接中...");
                        if (wifiListener != null) {
                            wifiListener.connState(state,"连接中...");
                        }
                    } else if (state == state.AUTHENTICATING) {
                        if (wifiListener != null) {
                            wifiListener.connState(state,"正在验证身份信息...");
                        }
                        Log.d(TAG,"正在验证身份信息...");
                    } else if (state == state.OBTAINING_IPADDR) {
                        if (wifiListener != null) {
                            wifiListener.connState(state,"正在获取IP地址...");
                        }
                        Log.d(TAG,"正在获取IP地址...");
                    } else if (state == state.FAILED) {
                        if (wifiListener != null) {
                            wifiListener.connState(state,"连接失败");
                        }
                        Log.d(TAG,"连接失败");
                    }
                }
            }else if (action.endsWith("android.net.wifi.WIFI_AP_STATE_CHANGED")){  //热点开启关闭广播


                int cstate = intent.getIntExtra("wifi_state", -1);
                Log.d(TAG,"热点啊  啊啊啊 啊啊啊 " + cstate);
                if(cstate == 13) {

                }if(cstate == 11  || cstate == 14) {

                }
            }
        }
    }

    private WifiReceiver mReceiver ;
    private WifiStateChangeListener wifiListener;
    public void registerReceiver(Context context,WifiStateChangeListener listener){
        if (mReceiver == null) {
            mReceiver = new WifiReceiver();
        }
        wifiListener = listener;
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        context.registerReceiver(mReceiver,filter);
    }

    public void unRegisterReceiver(Context context) {
        if (mReceiver != null)
            context.unregisterReceiver(mReceiver);
    }

   public interface WifiStateChangeListener {
        void connSuccess(WifiInfo wifiInfo );
        void scanSuccess(List<ScanResult> scanResults);
        void connState(NetworkInfo.DetailedState state ,String des);
        void disConn();
    }


}
