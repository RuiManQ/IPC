package com.example.ipctest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.providerTest.ProviderActivity;

public class MainActivity extends AppCompatActivity {

    private Button iPCTestButton;
    private Button broadcastTest;
    private Button providerTest;
    private Messenger mService;
    private Button testPlugin;
    private Button testService;
    private static final String TAG = "Activity_qiruimin";
    public static final String EXTRA_RESULT_CODE = "result_code";
    private static final String authority = "com.oplus.digitalkey.PluginProvider";
    private static final String METHOD_TEST1 = "notifyDeviceFound";
    private static final String EXTRA_KEY_SCAN_RESULT = "oplus_ccc_service_plugin_key_scan_result";

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testService = findViewById(R.id.testservice);
        testService.setOnClickListener(v -> testMyService());
        iPCTestButton = findViewById(R.id.button);
        iPCTestButton.setOnClickListener(v -> test());
        broadcastTest = findViewById(R.id.broadcasttest);
        broadcastTest.setOnClickListener(v -> mySendBroadcast());
        providerTest = findViewById(R.id.providerTest);
        providerTest.setOnClickListener(v -> goToProviderActivity());
        testPlugin = findViewById(R.id.pluginTest);
        testPlugin.setOnClickListener(v -> callProvider());
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
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_SERVICE:
                    Log.e("qiruimin", "handleMessage: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void bindIpcService() {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void goToBookActivity() {
        Intent intent = new Intent(this, BookManagerActivity.class);
        startActivity(intent);
    }

    private void gotowallet() {
        Intent intent = new Intent("oplus.intent.action.opluscarlink.CHILDREN_VERIFY");
        intent.setPackage("com.finshell.wallet");
        intent.putExtra("type", 10);
        sendBroadcast(intent);
        Log.e("qiruimin", "send broadcast to wallet: ");
    }

    private void test() {

    }

    private void goToProviderActivity() {
        Intent intent = new Intent(this, ProviderActivity.class);
        startActivity(intent);
    }

    private void scroll() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) iPCTestButton.getLayoutParams();
        params.bottomMargin += 200;
        params.topMargin -= 200;
        params.leftMargin += 200;
        params.rightMargin -= 200;
        iPCTestButton.requestLayout();

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void callProvider() {
        Log.e("qiruimin", "callProvider: ");
        try {

            Bundle scanBundle = new Bundle();
            scanBundle.putString(EXTRA_KEY_SCAN_RESULT, "oplus_ccc_service_plugin_key_scan_result");
            Bundle callResult = getApplicationContext().getContentResolver()
                    .call(authority, METHOD_TEST1, null, scanBundle);
            Log.e("qiruimin", "notifyDeviceFound: " + callResult.getInt(EXTRA_RESULT_CODE));
        } catch (Exception e) {
            Log.e("qiruimin", "callProvider: Error");
            e.printStackTrace();
        }
    }

    private void mySendBroadcast() {
        Log.e("qiruimin", "mySendBroadcast");
        Intent intent = new Intent();
        intent.setClassName("com.example.testplugin", "com.example.testplugin.MyReceiver");
        intent.setAction("com.example.testplugin");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

    private void testMyService() {
        Log.e(TAG, "testMyService: ");
        Intent intent = new Intent();
        intent.setClassName("com.oplus.digitalkey", "com.oplus.digitalkey.PluginService");
        intent.setAction("com.oplus.digitalkey.action.PluginService");
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e(TAG, "onServiceConnected: ccc");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }
}