package com.example.ipctest;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

public class AppUtils {
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static Boolean isMainProcess(Context context){
        return TextUtils.equals(Application.getProcessName(),context.getApplicationInfo().processName);
    }
}
