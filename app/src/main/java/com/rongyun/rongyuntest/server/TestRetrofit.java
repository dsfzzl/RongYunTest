package com.rongyun.rongyuntest.server;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.rongyun.rongyuntest.server.param.LoginParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.Result;
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

    public<T> Disposable login(String region, String phone, String password, final TextView textView) {
        final Disposable[] disposable = new Disposable[1];
        mRetrofitInterface.login(new LoginParams(region, phone, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<ResponseBody>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable[0] = d;
                        textView.setText("sfadfadsfadfa");
                    }

                    @Override
                    public void onNext(@NonNull Result<ResponseBody> responseBody) {
                        try {
                            Headers headers = responseBody.response().headers();
                            String s = headers.get("Date");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = headers.getDate("Date");
                            Date date1 = new Date(s);

                            textView.setText("时间：" +format.format(date1) + "  date: " + format.format(date));
                        } catch (Exception e) {
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


    public void downLoad(Context context) {
        mRetrofitInterface.download().subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        if (writeResponseBodyToDisk(responseBody)) {
                            Log.d("文件下载","文件下载成功了啊");
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

    private boolean writeResponseBodyToDisk(ResponseBody responseBody) {
        File dir = Environment.getExternalStorageDirectory();
        InputStream inputStream = null ;
        FileOutputStream out = null;

        try {
            inputStream = responseBody.byteStream();
            out = new FileOutputStream(new File(dir,"test.zip"));
            byte[] bufft = new byte[1024 *4];
            int len = 0;
            while ((len = inputStream.read(bufft)) != -1) {
                out.write(bufft,0,len);
            }

            out.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
