// IBookManager.aidl
package com.example.ipctest;

// Declare any non-default types here with import statements
import com.example.ipctest.IBook;
import com.example.ipctest.IOnNewBookArrivedListener;
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}