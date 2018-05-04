package com.rongyun.rongyuntest.server;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rongyun.rongyuntest.bean.TestBean;
import com.rongyun.rongyuntest.server.interceptor.CacheInterceptor;
import com.rongyun.rongyuntest.server.param.LoginParams;
import com.rongyun.rongyuntest.utils.StorageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2018/4/12.
 *
 */

public class TestRetrofit {
    private static final int CONNECT_TIME_OUT = 600;
    private static final long CACHE_SIZE = 10*10*1024*1024;

    private  Retrofit mRetrofit;
    private  RetrofitInterface mRetrofitInterface;
    private Apiserver mApiserver;
    private Context mContext;


    public TestRetrofit(Context context) {
        mContext = context;

        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIME_OUT,TimeUnit.SECONDS)
                .writeTimeout(CONNECT_TIME_OUT,TimeUnit.SECONDS)
                .addInterceptor(new CacheInterceptor(mContext))
               // .addNetworkInterceptor(new CacheInterceptor(mContext))
                .cache(new Cache(StorageUtils.getCacheDirectory(mContext),CACHE_SIZE))
                .build();

        mRetrofit = new Retrofit.Builder().baseUrl("http://api.sealtalk.im/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mRetrofitInterface = mRetrofit.create(RetrofitInterface.class);
        //http://c.m.163.com/nc/article/headline/T1348647909107/60-20.html
        mRetrofit = new Retrofit.Builder().baseUrl("http://c.m.163.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApiserver = mRetrofit.create(Apiserver.class);
    }
    public TestRetrofit(Context context,String baseUrl) {
        mContext = context;
        mRetrofit = new Retrofit.Builder().baseUrl("http://testproxy.jfshare.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mRetrofitInterface = mRetrofit.create(RetrofitInterface.class);
    }

    public void getNewsList() {
        String type = "headline";
        String newsId = "T1348647909107";
        int page = 60;
        mApiserver.getNewsList(type,newsId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               /* .compose(new ResponseBodyToBean<Map>(HashMap.class))
                .subscribe(new Observer<Map>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Map map) {
                        Map<String, List<NewsInfo>> map1 = map;
                        List<NewsInfo> newsInfos = map1.get("T1348647909107");
                        NewsInfo newsInfo = newsInfos.get(0);
                        Log.e("onNext","成功了啊 ： " +newsInfo.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });*/

                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<ResponseBody> requestBodyResponse) {
                        try {
                            if (!requestBodyResponse.isSuccessful()) {
                                Log.e("onNext","出错了啊： " +requestBodyResponse.errorBody().string());
                            }

                           /* Headers headers = responseBody.response().headers();
                            String s = headers.get("Date");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = headers.getDate("Date");
                            Date date1 = new Date(s);*/
                            TestBean testBean = new TestBean();
                            testBean.age = "15";
                            testBean.name = "zzl";
                            testBean.sex = "nv";
                            testBean.hhhh = "hhhh";
                            Gson gson = new Gson();
                            String s = gson.toJson(testBean);
                            Type type = new TypeToken<TestBean>() {
                            }.getType();
                            TestBean o = gson.fromJson(s, type);
                            Log.e("onNext","成功了啊 ： " +o.age);
                           /* Map<String, List<NewsInfo>> map = gson.fromJson(requestBodyResponse.body().string(), type);
                            Log.e("onNext","成功了啊 ： " +map.toString());

                            List<NewsInfo> newsInfos = map.get("T1348647909107");
                            NewsInfo newsInfo = newsInfos.get(0);
                            Log.e("onNext","成功了啊 ： " +newsInfo.toString());*/
                            //textView.setText("时间：" +format.format(date1) + "  date: " + format.format(date) + "  数据啊： " +responseBody.body().string() );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TestRetrofit","出错了");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public Disposable login(String region, String phone, String password, final TextView textView) {
        final Disposable[] disposable = new Disposable[1];
        mRetrofitInterface.login(new LoginParams(region, phone, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable[0] = d;
                        textView.setText("ggggsdssdgggfa");
                    }

                    @Override
                    public void onNext(@NonNull Response<ResponseBody> responseBody) {
                        try {
                            if (!responseBody.isSuccessful()) {
                                Log.e("onNext","出错了啊： " +responseBody.errorBody().string());
                            }

                           /* Headers headers = responseBody.response().headers();
                            String s = headers.get("Date");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = headers.getDate("Date");
                            Date date1 = new Date(s);*/

                            textView.setText(/*"时间：" +format.format(date1) + "  date: " + format.format(date) + */"  数据啊： " +responseBody.body().string() );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TestRetrofit","登录出错了");
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

    private <T> Observable<T> observe(Observable<T> observable){
        Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return null;

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

    public void upImage() {
        Map<String,String> params = new HashMap<>();
        params.put("code","200");
        params.put("shide","不是吧");
        params.put("shibushia","肯定是啊");
        Gson gson = new Gson();
        String s = gson.toJson(params);
        Log.e("看看参数啊","参数： " + s);
        File file = new File(Environment.getExternalStorageDirectory(),"xhdpi/pay_rb_checked.png");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-png"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("Filedata",file.getName(),requestBody);
        mRetrofitInterface.upImage(part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<ResponseBody>>() {
                    @Override
                    public void accept(Response<ResponseBody> requestBodyResponse) throws Exception {
                        Log.e("我在这里啊","请求成功了啊  "+requestBodyResponse.body().string());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("我在这里啊","请求失败了啊 "+throwable.getMessage());
                    }
                });
    }


    private RequestBody toRequestBody(Map<String,Object> map) {
        Gson gson = new Gson();
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(map));
    }



    //转换器
     class ResponseBodyToBean<T> implements ObservableTransformer<Response<ResponseBody>,T> {

        @Override
        public ObservableSource<T> apply(@NonNull Observable<Response<ResponseBody>> upstream) {
            return upstream.flatMap(new Function<Response<ResponseBody>, ObservableSource<T>>() {
                @Override
                public ObservableSource<T> apply(@NonNull Response<ResponseBody> responseBody) throws Exception {
                    Gson gson = new Gson();
                    Type type = new TypeToken<T>(){}.getType();
                    T t = gson.fromJson(responseBody.body().string(), type);
                    return Observable.just(t);
                }
            });
        }
    }


    public void test() {
        String url = "http://book.my716.com/getBooks.aspx?method=content&bookId=682770&chapterFile=773110_201507161331431190_12.txt";
        mApiserver.getChapterInfoPackage(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            Log.e("返回的数据","responseBody : " + responseBody.string());
                        } catch (IOException e) {
                            Log.e("返回的数据","responseBody : " + "出错");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("返回的数据","onError : " + "出错");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
