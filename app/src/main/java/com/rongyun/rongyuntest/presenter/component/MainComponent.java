package com.rongyun.rongyuntest.presenter.component;

import com.rongyun.rongyuntest.ui.activity.MainActivity;

import dagger.Component;


/**
 * Created by Administrator on 2018/4/19.
 */

@Component(modules = MainModule.class)
public interface MainComponent {

    void inject(MainActivity mainView);
}
