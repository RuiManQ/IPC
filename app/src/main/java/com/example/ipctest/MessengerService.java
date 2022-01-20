package com.example.ipctest;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    public MessengerService() {
    }
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg){

            if (msg.what == MyConstants.MSG_FROM_CLIENT) {
                Log.e("qiruimin", "handleMessage: " + msg.getData().getString("name"));
                Messenger client = msg.replyTo;
                Message replyMessage = Message.obtain();
                replyMessage.what = MyConstants.MSG_FROM_SERVICE;
                Bundle bundle = new Bundle();
                bundle.putString("reply","已收到消息");
                replyMessage.setData(bundle);
                try {
                    client.send(replyMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            } else {
                super.handleMessage(msg);
            }
        }
    }
    private final Messenger mMessenger = new Messenger(new MessengerHandler());
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}