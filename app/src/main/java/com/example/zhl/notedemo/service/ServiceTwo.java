package com.example.zhl.notedemo.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.zhl.notedemo.ui.MainActivity;
import com.example.zhl.notedemo.utils.ToastUtils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐播放后台服务
 * Created by wcy on 2015/11/27.
 */
public class ServiceTwo extends Service {
    private static final String TAG = "Service";

    ClipboardManager clipboardManager;
    public class PlayBinder extends Binder {
        public ServiceTwo getService() {
            return ServiceTwo.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getClass().getSimpleName());
        /*clipboardManager =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

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
        });*/
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startCommand(Context context) {
        Intent intent = new Intent(context, ServiceTwo.class);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread.start();
        return START_REDELIVER_INTENT;
    }
    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    Log.e(TAG, "ServiceTwo Run: " + System.currentTimeMillis());
                    boolean b = MainActivity.isServiceWorked(ServiceTwo.this, "com.example.zhl.notedemo.service.ServiceOne");
                    if(!b) {
                        MainActivity.mPreviousText = "ServiceTwo已失效"+new Date();
                        MainActivity.instance.doPaste();
                        Intent service = new Intent(ServiceTwo.this, ServiceOne.class);
                        startService(service);
                    }
                }
            };
            timer.schedule(task, 0, 1000);
        }
    });

}
