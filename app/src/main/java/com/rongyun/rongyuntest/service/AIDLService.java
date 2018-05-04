package com.rongyun.rongyuntest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rongyun.rongyuntest.AddBookListener;
import com.rongyun.rongyuntest.Book;
import com.rongyun.rongyuntest.BookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class AIDLService extends Service {
    private final static String TAG = "AIDLService";
    private List<Book> mBooks = new ArrayList<>();
    //这个是远程集合，是线程安全的。
    private RemoteCallbackList<AddBookListener> mRemoteCallbackList = new RemoteCallbackList<>();
    private final BookManager.Stub mBookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            //这里是在Binder 线程池中调用，不在UI线程中，可以做耗时操作，需要考虑线程安全 。
            synchronized (AIDLService.class) {
                if (mBooks != null)
                    return mBooks;
                return new ArrayList<>();
            }
        }

        @Override
        public Book getBook() throws RemoteException {
            synchronized (AIDLService.class) {
                if (mBooks != null){
                    Book book = new Book();
                    book.name = "我是新建的book";
                    book.code = "12345555";
                    Log.e(TAG,"getBook : " + book.toString());
                    return book;
                }
                return new Book();
            }
        }

        @Override
        public void setBookPrice(Book book, int price) throws RemoteException {
            synchronized (AIDLService.class) {
                if (book != null) {
                    Log.e(TAG,"setBookPrice : " + book.toString());
                    mBooks.add(book);
                }
            }
        }

        @Override
        public void setBookName(Book book, String name) throws RemoteException {

        }

        @Override
        public void addBookIn(Book book) throws RemoteException {
            synchronized (AIDLService.class) {
                if (book != null) {
                    Log.e(TAG,"addBookIn : " + book.toString());
                    book.code = "1";
                    mBooks.add(book);
                }
            }
        }

        @Override
        public void addBookOut(final Book book) throws RemoteException {
            synchronized (AIDLService.class) {
                if (book != null) {
                    Log.e(TAG,"addBookOut : " + book.toString());
                    new Thread(){
                        @Override
                        public void run() {
                            SystemClock.sleep(2000);
                            book.code = "1";
                        }
                    }.start();
                    mBooks.add(book);
                }
            }
        }

        @Override
        public void addBookInout(final Book book) throws RemoteException {
            synchronized (AIDLService.class) {
                if (book != null) {
                    Log.e(TAG,"addBookInout : " + book.toString());
                    book.code = "1";
                    mBooks.add(book);
                }
            }
        }

        @Override
        public void registerAddBookListener(AddBookListener listener) throws RemoteException {

        }

        @Override
        public void unrgisterAddBookListener(AddBookListener listener) throws RemoteException {

        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBookManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.name = "Android开发艺术探索";
        book.code = "28";
        mBooks.add(book);

        Log.e(TAG,"onCreate :  服务端启动了");
    }
}
