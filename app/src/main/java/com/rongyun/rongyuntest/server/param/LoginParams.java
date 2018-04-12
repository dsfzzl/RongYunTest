package com.rongyun.rongyuntest.server.param;

/**
 * Created by Administrator on 2018/4/12.
 */

public class LoginParams {
    public String region;
    public String phone;
    public String password;

    public LoginParams(String region, String phone, String password) {
        this.region = region;
        this.phone = phone;
        this.password = password;
    }
}
