package com.example.zhl.notedemo.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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
                if(MainActivity.mPreviousText.equals(item.getText().toString())){ return;}
                else{
                    MainActivity.mPreviousText = item.getText().toString();
                    MainActivity.instance.doPaste();
                }
            }
        });
        startForeground( 0x111, buildNotification(this));
    }

    private Notification buildNotification(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent);
        return builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startCommand(Context context) {
        Intent intent = new Intent(context, PasteCopyService.class);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

}
