package com.example.zhl.notedemo.ui;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.zhl.notedemo.utils.ToastUtils;

public class PasteCopyService extends Service {
    public class MyBinder extends Binder {
        PasteCopyService getService() {
            return PasteCopyService.this;
        }
    }
    ClipboardManager clipboardManager;

    private String mPreviousText = "";
    @Override
    public void onCreate() {
        super.onCreate();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
