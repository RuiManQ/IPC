package com.example.ipctest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button iPCTestButton;
    private Button bookTest;
    private Messenger mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iPCTestButton = findViewById(R.id.button);
        iPCTestButton.setOnClickListener(v -> gotowallet());
        bookTest = findViewById(R.id.button3);
        bookTest.setOnClickListener(v -> goToBookActivity());
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Message msg = Message.obtain();
            msg.what = MyConstants.MSG_FROM_CLIENT;
            Bundle data = new Bundle();
            data.putString("name", "qiruimin");

            msg.setData(data);
            msg.replyTo = mGetReplyMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case MyConstants.MSG_FROM_SERVICE:
                    Log.e("qiruimin", "handleMessage: "+msg.getData().getString("reply") );
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void bindIpcService(){
        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }
    private void goToBookActivity() {
        Intent intent = new Intent(this,BookManagerActivity.class);

        startActivity(intent);
    }
    private void gotowallet() {
        Intent intent = new Intent("oplus.intent.action.opluscarlink.CHILDREN_VERIFY");
        intent.setPackage("com.finshell.wallet");
        intent.putExtra("type",10);
        sendBroadcast(intent);
        Log.e("qiruimin", "send broadcast to wallet: ");
    }
}