package com.example.zhl.notedemo.ui;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by Administrator on 2016/3/11.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setTheme();
    }

    public void setTheme(){
        SharedPreferences spf = getSharedPreferences("config",MODE_PRIVATE);
        Boolean isNightMode = spf.getBoolean("theme_value",false);
        if (isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
