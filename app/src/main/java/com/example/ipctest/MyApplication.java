package com.example.ipctest;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class MyApplication extends Application {

    private Context context;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate() {
        context = getApplicationContext();
        Log.d("qiruimin", "CarApplication create"+getProcessName());
        super.onCreate();
        if(AppUtils.isMainProcess(context)){
            Log.e("qiruimin", "onCreate: 主进程" );
        }else{
            Log.e("qiruimin", "onCreate: 非主进程" );
        }
    }
}
