package com.example.zhl.notedemo.ui;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.example.zhl.notedemo.R;
import com.example.zhl.notedemo.service.ServiceOne;
import com.example.zhl.notedemo.utils.PermissionReq;

import java.util.ArrayList;


/**
 * 基类<br>
 * 如果继承本类，需要在 layout 中添加 {@link Toolbar} ，并将 AppTheme 继承 Theme.AppCompat.NoActionBar 。
 * Created by wcy on 2015/11/26.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Handler handler;
    public ServiceOne playService2;
    private ProgressDialog progressDialog;
    private ServiceConnection serviceConnection2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setSystemBarTransparent();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        handler = new Handler(Looper.getMainLooper());
        bindService2();

    }
    private void bindService2() {
        Intent intent = new Intent();
        intent.setClass(this, ServiceOne.class);
        serviceConnection2 = new PlayServiceConnection2();
        bindService(intent, serviceConnection2, Context.BIND_AUTO_CREATE);
    }

    private void setSystemBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // LOLLIPOP解决方案
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // KITKAT解决方案
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showProgress() {
        showProgress(getString(R.string.loading));
    }

    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void cancelProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    private class PlayServiceConnection2 implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playService2 = ((ServiceOne.PlayBinder) service).getService();
            onServiceBound2();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getClass().getSimpleName(), "service disconnected");
        }
    }
    protected void onServiceBound2() {
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        if (serviceConnection2 != null) {
            unbindService(serviceConnection2);
        }
        super.onDestroy();
    }
}
