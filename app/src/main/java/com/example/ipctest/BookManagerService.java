package com.example.ipctest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookManagerService extends Service {
    public BookManagerService() {
    }
    private static final String TAG = "qiruimin";
    private Boolean mIsServiceDestroyed = false;
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList = new CopyOnWriteArrayList<>();
    private Thread mNewBookThread;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() {
            SystemClock.sleep(6000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            for (int i = 0; i < mListenerList.size(); i++) {
                if (mListenerList.get(i).getListenerId() == listener.getListenerId()) {
                    Log.e(TAG, "listener already exist! ");
                    return;
                }
            }
            mListenerList.add(listener);
            Log.e(TAG, "registerListener: "+mListenerList.size());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            for(int i=0;i<mListenerList.size();i++){
                if(mListenerList.get(i).getListenerId() == listener.getListenerId()){
                    IOnNewBookArrivedListener tempListen = mListenerList.get(i);
                    mListenerList.remove(tempListen);
                    Log.e(TAG, "unregisterListener:Success "+mListenerList.size() );
                    return;
                }
            }
            Log.e(TAG, "listener not found! ");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"数据结构"));
        mBookList.add(new Book(2,"操作系统"));
        mNewBookThread = new Thread(new ServiceWorker());
        mNewBookThread.start();
    }
    private class ServiceWorker implements Runnable {


        @Override
        public void run() {
            while(!mIsServiceDestroyed) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() +1;
                Book newBook = new Book(bookId,"new Book#"+bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private void onNewBookArrived(Book newBook) throws RemoteException {
        mBookList.add(newBook);
        Log.e(TAG, "onNewBookArrived: " );
        for(int i=0;i<mListenerList.size();i++){
            IOnNewBookArrivedListener listener  = mListenerList.get(i);
            Log.d(TAG, "Notify NewBookArrived ");
            listener.onNewBookArrived(newBook);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "MyService.onUnbind");
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "MyService.onDestroy");
        mNewBookThread.stop();
        super.onDestroy();
    }
}