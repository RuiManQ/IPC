// IOnNewBookArrivedListener.aidl
package com.example.ipctest;

// Declare any non-default types here with import statements
import com.example.ipctest.IBook;
interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book newBook);
}