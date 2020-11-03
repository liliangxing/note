package com.example.zhl.notedemo.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.util.Log;

import com.example.zhl.notedemo.ui.MainActivity;
import com.example.zhl.notedemo.utils.ToastUtils;

import java.util.ArrayList;

public class RecoveryHandleService extends IntentService {
    private static volatile boolean fJa;

    public static class InnerService extends Service {
        public void onCreate() {
            super.onCreate();
            try {
                startForeground(-1119860829, new Notification());
            } catch (Throwable th) {

            }
            stopSelf();
        }

        public void onDestroy() {
            stopForeground(true);
            super.onDestroy();
        }

        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public RecoveryHandleService() {
        super(RecoveryHandleService.class.getName());
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private ClipboardManager clipboardManager;
    protected final void onHandleIntent(Intent intent) {
                clipboardManager =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                    @Override
                    public void onPrimaryClipChanged() {
                        ClipData clipData = clipboardManager.getPrimaryClip();
                        ClipData.Item item = clipData.getItemAt(0);
                        if(null == item || null == item.getText()){
                            ToastUtils.show("获取剪贴板失败");
                            return;
                        }
                        if(MainActivity.mPreviousText.equals(item.getText().toString())){ return;}
                        else{
                            MainActivity.mPreviousText = item.getText().toString();
                            MainActivity.instance.doPaste();
                        }
                    }
                });
     }

}
