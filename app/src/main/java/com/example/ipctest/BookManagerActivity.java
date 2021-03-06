package com.example.ipctest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {
    private IBookManager remoteBookManager;
    private static final String TAG = "qiruimin";
    private Button stopBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        stopBtn = findViewById(R.id.button2);
        stopBtn.setOnClickListener(v -> stopObserver());
        Intent intent = new Intent(this,BookManagerService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {

            switch (msg.what){
                case MyConstants.MSG_NEW_BOOK_ARRIVED:
                    Log.e(TAG, "handleMessage: receive new book " +msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: " );
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                remoteBookManager = bookManager;
                bookManager.addBook(new Book(3,"计算机网络"));
                bookManager.registerListener(mOnNewBookArrivedListener1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                List<Book> list = bookManager.getBookList();
                for(int i=0;i<list.size();i++){
                Log.i(TAG, "Book List: "+ list.get(i).toString());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.e(TAG, "onServiceDisconnected: ");
        }
    };
    @Override
    protected void onDestroy() {
        if(remoteBookManager != null&&remoteBookManager.asBinder().isBinderAlive()){
            Log.e(TAG, "onDestroy & unregister listener ");
            try {
                remoteBookManager.unregisterListener(mOnNewBookArrivedListener1);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(mServiceConnection);

        }
        super.onDestroy();
    }
    private IOnNewBookArrivedListener mOnNewBookArrivedListener1 = new IOnNewBookArrivedListener.Stub() {
        @Override
        public int getListenerId() throws RemoteException {
            return mOnNewBookArrivedListener1.hashCode();
        }

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            mHandler.obtainMessage(MyConstants.MSG_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };

    private void stopObserver(){
        Log.e(TAG, "stopConnection: " );
//        stopService(new Intent(this,BookManagerService.class));
//        try {
//            unbindService(mServiceConnection);
//        }catch (Exception e){
//            Log.e(TAG, "Exception e" );
//        }

//        stopService(new Intent(this,BookManagerService.class));
    }
}