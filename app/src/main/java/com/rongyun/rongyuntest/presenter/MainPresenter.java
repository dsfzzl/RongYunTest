package com.rongyun.rongyuntest.presenter;

import com.rongyun.rongyuntest.ui.activity.viewi.MainView;

/**
 * Created by Administrator on 2018/4/19.
 */

public class MainPresenter implements IMainPresenter {
    private MainView mMainView;


    public MainPresenter() {

    }

    @Override
    public String get() {
        return "woshiwoshi";
    }
}
