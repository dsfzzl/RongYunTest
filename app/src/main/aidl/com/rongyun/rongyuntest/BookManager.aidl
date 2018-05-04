// BookManager.aidl
package com.rongyun.rongyuntest;

// Declare any non-default types here with import statements
import com.rongyun.rongyuntest.Book;
import com.rongyun.rongyuntest.AddBookListener;
interface BookManager {

    List<Book> getBooks();
    Book getBook();
    //传参时除了Java基本类型以及String，CharSequence之外的类型
        //都需要在前面加上定向tag，具体加什么量需而定
        void setBookPrice(in Book book , int price);
        void setBookName(in Book book , String name);
        void addBookIn(in Book book);
       void addBookOut(out Book book);
       void addBookInout(inout Book book);
       void registerAddBookListener(AddBookListener listener);
       void unrgisterAddBookListener(AddBookListener listener);
}
