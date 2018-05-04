package com.rongyun.rongyuntest.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rongyun.rongyuntest.Book;
import com.rongyun.rongyuntest.BookManager;
import com.rongyun.rongyuntest.R;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/23.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "SecondActivity";
    private BookManager mBookManager;
    private boolean isBind;
    private ServiceConnection mServiceConnection;
    private Book mBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.tv_get_book).setOnClickListener(this);
        findViewById(R.id.tv_get_books).setOnClickListener(this);
        findViewById(R.id.tv_set_book_price).setOnClickListener(this);
        findViewById(R.id.tv_add_book_in).setOnClickListener(this);
        findViewById(R.id.tv_add_book_out).setOnClickListener(this);
        findViewById(R.id.tv_add_book_inout).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isBind) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBind) {
            if (mServiceConnection != null)
                //mBookManager.asBinder().isBinderAlive()
                unbindService(mServiceConnection);
        }
    }

    @Override
    public void onClick(View view) {
        if (!isBind){
            attemptToBindService();
        }
        try {
            Book book ;
            switch (view.getId()) {
                case R.id.tv_get_books:
                    Observable
                            .create(new ObservableOnSubscribe<List<Book>>() {
                                @Override
                                public void subscribe(@NonNull ObservableEmitter<List<Book>> emitter) throws Exception {
                                    List<Book> books = mBookManager.getBooks();
                                    emitter.onNext(books);
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .flatMap(new Function<List<Book>, ObservableSource<Book>>() {
                                @Override
                                public ObservableSource<Book> apply(@NonNull List<Book> books) throws Exception {
                                    return Observable.fromIterable(books);
                                }
                            })
                            .subscribe(new Consumer<Book>() {
                                @Override
                                public void accept(Book book) throws Exception {

                                }
                            });

                    break;
                case R.id.tv_get_book:
                    book = mBookManager.getBook();
                    Log.e(TAG,"tv_get_book" + book.toString());
                    break;
                case R.id.tv_set_book_price:
                    book = new Book();
                    book.name = "客户端";
                    book.code = "2020120";
                    mBookManager.setBookPrice(book,12);

                    Log.e(TAG,"tv_set_book_price" + mBook.toString());
                    break;
                case R.id.tv_add_book_in:
                    book = new Book();
                    book.name = "客户端 book_in";
                    book.code = "2020120";
                    mBookManager.addBookIn(book);
                    Log.e(TAG,"tv_add_book_in" + book.toString());
                    break;
                case R.id.tv_add_book_out:
                    book = new Book();
                    book.name = "客户端 book_out";
                    book.code = "2020120";
                    mBookManager.addBookOut(book);
                    Log.e(TAG,"tv_add_book_out" + book.toString());
                    mBook = book;
                    break;
                case R.id.tv_add_book_inout:
                    book = new Book();
                    book.name = "客户端 book_inout";
                    book.code = "2020120";
                    mBookManager.addBookInout(book);
                    Log.e(TAG,"tv_add_book_inout" + book.toString());
                    break;

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void attemptToBindService(){
        Intent intent = new Intent();
        intent.setAction("com.rongyun.rongyuntest.aidl");
        intent.setPackage("com.rongyun.rongyuntest");
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.e(TAG, "onServiceConnected");
                mBookManager = BookManager.Stub.asInterface(iBinder);
                isBind = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e(TAG, "onServiceDisconnected");
                isBind = false;
            }
        };

        if (getPackageManager().resolveService(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }else {
            Log.d(TAG,"没有对应的 Intent");
        }
    }
}
