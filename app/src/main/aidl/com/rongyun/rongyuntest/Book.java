package com.rongyun.rongyuntest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/23.
 */

public class Book implements Parcelable{

    public String name;
    public String code;
    public Book(){
    }

    protected Book(Parcel in) {
        name = in.readString();
        code = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(code);
    }
    //如果要支持为 out 或者 inout 的定向 tag 的话，还需要实现 readFromParcel()
    // 方法——而这个方法其实并没有在 Parcelable 接口里面，所以需要我们从头写
    public void readFromParcel(Parcel dest) {
        //注意，此处的读值顺序应当是和writeToParcel()方法中一致的
        name = dest.readString();
        code = dest.readString();
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
