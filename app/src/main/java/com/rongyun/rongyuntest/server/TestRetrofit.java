package com.rongyun.rongyuntest.server;

import android.util.Log;
import android.widget.TextView;

import com.rongyun.rongyuntest.server.param.LoginParams;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by Administrator on 2018/4/12.
 */

public class TestRetrofit {

    private final Retrofit mRetrofit;
    private final RetrofitInterface mRetrofitInterface;

    public TestRetrofit() {
        mRetrofit = new Retrofit.Builder().baseUrl("http://api.sealtalk.im/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mRetrofitInterface = mRetrofit.create(RetrofitInterface.class);
    }

    public Disposable login(String region, String phone, String password, final TextView textView) {
        final Disposable[] disposable = new Disposable[1];
        mRetrofitInterface.login(new LoginParams(region, phone, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable[0] = d;
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            textView.setText(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        if (disposable[0] != null) {
            Log.e("disposable","disposable : " + disposable[0].isDisposed());
        }
        return disposable[0];
    }

    public void login2(String region, String phone, String password, final TextView textView) {
        final Call<ResponseBody> call = mRetrofitInterface.login2(new LoginParams(region, phone, password));

        Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ResponseBody> emitter) throws Exception {
                Response<ResponseBody> execute = call.execute();
                int code = execute.code();
                Log.e("subscribe","code : " + code);
                emitter.onNext(execute.body());
                emitter.onComplete();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("onSubscribe","code : " + d.isDisposed());
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            textView.setText(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private <T>void observe(final Call<T> call, Observer<? super T> observer){
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {
                Response<T> execute = call.execute();
                int code = execute.code();
                if (code == 200) {
                    emitter.onNext(execute.body());
                }else {

                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
