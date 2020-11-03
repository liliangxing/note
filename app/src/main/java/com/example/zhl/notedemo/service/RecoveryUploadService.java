package com.example.zhl.notedemo.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class RecoveryUploadService extends IntentService {
    private static final String TAG = "Recovery.RecoveryUploadService";

    public RecoveryUploadService() {
        super(RecoveryUploadService.class.getName());
    }

    public static synchronized void startAlarm(Context context, String str) {
        synchronized (RecoveryUploadService.class) {

        }
    }

    protected void onHandleIntent(Intent intent) {

        stopSelf();
    }

    public boolean tryToUploadData() {
        return true;
    }
}

