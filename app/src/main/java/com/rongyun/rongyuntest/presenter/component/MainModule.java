package com.rongyun.rongyuntest.presenter.component;

import com.rongyun.rongyuntest.presenter.IMainPresenter;
import com.rongyun.rongyuntest.presenter.MainPresenter;
import com.rongyun.rongyuntest.ui.activity.viewi.MainView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/4/19.
 */
@Module
public class MainModule {
    private MainView mMainView;

    public MainModule(MainView mainView) {
        mMainView = mainView;
    }

    @Provides
    public MainView getMainView(){
        return mMainView;
    }
    @Provides
    public IMainPresenter getMainPresenter (){
        return new MainPresenter();
    }
}
