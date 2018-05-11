package com.rongyun.rongyuntest.bean;

/**
 * Created by Administrator on 2018/5/10.
 */

public class TokenBean {
    /*  "access_token": "24.33452d90850f6ea79b0fe6d74ffb45dc.2592000.1528528950.282335-11220309",
    "session_key": "9mzdCPe4szn1ymvi4jHUzNPGC6hjON08y8OjdQ73x+6GS8eBquTCQpW+J+Hh1nBmH3NP/2C/RT+lA8XbTg4hkiaHlXEBwQ==",
    "scope": "public brain_all_scope vis-faceverify_FACE_V3 wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower lpq_开放 cop_helloScope ApsMis_fangdi_permission smartapp_snsapi_base",
    "refresh_token": "25.42d41b1a9cd3cb0dde561a689eed6bbe.315360000.1841296950.282335-11220309",
    "session_secret": "52b33e249336ebf4816e476506efd6f8",
    "expires_in": 2592000*/

    public String access_token;
    public String session_key;
    public String scope;
    public String refresh_token;
    public String session_secret;
    public String expires_in;

    @Override
    public String toString() {
        return "TokenBean{" +
                "access_token='" + access_token + '\'' +
                ", session_key='" + session_key + '\'' +
                ", scope='" + scope + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", session_secret='" + session_secret + '\'' +
                ", expires_in='" + expires_in + '\'' +
                '}';
    }
}
