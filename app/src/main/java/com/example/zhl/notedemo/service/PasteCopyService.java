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

/**
 * 音乐播放后台服务
 * Created by wcy on 2015/11/27.
 */
public class PasteCopyService extends Service {
    private static final String TAG = "Service";

    ClipboardManager clipboardManager;

    private String mPreviousText = "";
    public class PlayBinder extends Binder {
        public PasteCopyService getService() {
            return PasteCopyService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getClass().getSimpleName());
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
                if(mPreviousText.equals(item.getText().toString())){ return;}
                else{
                    mPreviousText = item.getText().toString();
                    MainActivity.instance.doPaste();
                }
            }
        });
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public static void startCommand(Context context, String action) {
        Intent intent = new Intent(context, PasteCopyService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {

            }
        }
        return START_NOT_STICKY;
    }
}
