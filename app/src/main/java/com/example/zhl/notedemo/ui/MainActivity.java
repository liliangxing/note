package com.example.zhl.notedemo.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhl.notedemo.R;
import com.example.zhl.notedemo.db.NoteDb;
import com.example.zhl.notedemo.utils.NoteUtil;
import com.example.zhl.notedemo.utils.PermissionReq;
import com.example.zhl.notedemo.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private Button note_new,cancel,choseAll,delete;
    private EditText searchEdit;
    private ListView mListView;
    private MyAdapter adapter;
    private NoteDb noteDb;
    private Cursor cursor;
    private static String[] PROJECTION = new String[]{"title","date"};
    public static Map<Long,Boolean> recordCursorIdStatus = new HashMap<Long,Boolean>();
    public static Map<Integer,Boolean> recordStatus = new HashMap<Integer,Boolean>();

    private LinearLayout mLinearLayout;
    private int count;
    private Long selectId;

    private int isHomeFlag = 1;

    private Toolbar toolbar;

    private  ClipboardManager clipboardManager;
    private  ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener;


    private String mPreviousText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("全部");
        cancel = (Button) findViewById(R.id.cancel);
        choseAll = (Button) findViewById(R.id.chose_all);
        delete = (Button) findViewById(R.id.delete);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        clipboardManager =(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        checkPermission();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){

            doCreate();

    }
    private void doCreate(){
        noteDb = NoteDb.getInstance(this);
        noteDb.updateDefaultClass();
        //cursor = noteDb.db.query("note",null,null,null,null,null,null);
        mListView = (ListView) findViewById(R.id.list_view);
        cursor = noteDb.queryAll();
        adapter = new MyAdapter(MainActivity.this,cursor);
        mListView.setAdapter(adapter);

        note_new = (Button) findViewById(R.id.note_new);
        note_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
                startActivityForResult(intent,100);
            }
        });

/*        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //长按Item进入多选模式
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.ismultiMode = true;
                mLinearLayout.setVisibility(View.VISIBLE);
                note_new.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        //点按Item进行选择
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.ismultiMode){
                    CheckBox cb = (CheckBox) view.findViewById(R.id.check);
                    Boolean isCheck = !cb.isChecked();
                    cb.setChecked(isCheck);
                    if (isCheck){
                        count++;
                    }else {
                        count--;
                    }
                    delete.setText("删除(" + count + ")");
                    selectId = adapter.getItemId(position);
                    recordStatus.put(position,isCheck);
                    recordCursorIdStatus.put(selectId,isCheck);
                }else {
                    //Toast.makeText(MainActivity.this, "弹出编辑界面" + position, Toast.LENGTH_SHORT).show();
                    Long editId = adapter.getItemId(position);
                    Cursor adapterCursor = adapter.getCursor();
                    adapterCursor.moveToPosition(position);
                    String edittitle = adapterCursor.getString(adapterCursor.getColumnIndex("title"));
                    String editcontent = adapterCursor.getString(adapterCursor.getColumnIndex("content"));
                    String editdate = adapterCursor.getString(adapterCursor.getColumnIndex("date"));
                    String editclass = adapterCursor.getString(adapterCursor.getColumnIndex("class"));
                    Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
                    intent.putExtra("edittitle",edittitle);
                    intent.putExtra("editcontent",editcontent);
                    intent.putExtra("editdate",editdate);
                    intent.putExtra("editclass",editclass);
                    startActivityForResult(intent,101);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.ismultiMode) {
                    cancelAction();
                }
            }
        });

        choseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.ismultiMode){
                    adapter.choseAll = true;
                    Cursor cursor = noteDb.queryAll();
                    if (cursor.moveToFirst()){
                        do {
                            int i = 0;
                            Long id = cursor.getLong(cursor.getColumnIndex("_id"));
                            recordStatus.put(0,true);
                            recordCursorIdStatus.put(id,true);
                            i++;
                        }while (cursor.moveToNext());
                    }
                    cursor.close();

                    adapter.notifyDataSetChanged();
                    delete.setText("删除(" + adapter.getCount() + ")");
                    Log.d("count", adapter.getCount() + "");
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordCursorIdStatus.isEmpty()){
                    ToastUtils.show("请选择");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.tips);
                builder.setMessage(R.string.delete_tips);
                builder.setPositiveButton(R.string.delete_tips_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();


            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //Toast.makeText(MainActivity.this,"test",Toast.LENGTH_SHORT).show();
                searchEdit.setVisibility(View.GONE);
                String Categories = searchEdit.getText().toString();
                Cursor cursor = noteDb.search(Categories);
                adapter.changeCursor(cursor);
                adapter.notifyDataSetChanged();
                searchEdit.setText("");
                note_new.setVisibility(View.VISIBLE);
                isHomeFlag = 2;
                return true;
            }
        });

        ToastUtils.init(this);
        onPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void  onPrimaryClipChanged() {
                ClipData clipData = clipboardManager.getPrimaryClip();
                ClipData.Item item = clipData.getItemAt(0);
                if(null == item || null == item.getText()){
                    ToastUtils.show("获取剪贴板失败");
                    return;
                }
                if(mPreviousText.equals(item.getText().toString())){ return;}
                else{
                    mPreviousText = item.getText().toString();
                    doPaste();
                }
            }
        };
        clipboardManager.addPrimaryClipChangedListener(onPrimaryClipChangedListener);
    }

    @Override
    public void onDestroy() {
        clipboardManager.removePrimaryClipChangedListener(onPrimaryClipChangedListener);
        super.onDestroy();
    }

    private void doPaste(){
        //获取剪贴板管理器：
        ClipData cmData = clipboardManager.getPrimaryClip();
        String content = "";
        if(null != cmData) {
            ClipData.Item item = cmData.getItemAt(0);
            content = item.getText().toString();
        }
        if(content.length()>= 6) {
            String tempClass = EditNoteActivity.listClass[0];
            String tempDate = NoteUtil.getDate();
            noteDb.saveNote(null, content, tempDate, tempClass);
            doRefresh(getString(R.string.app_name));
            ToastUtils.show("您有6个字以上新的剪贴内容，已加进笔记！");
        }
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }else {
            doCreate();
        }
    }

    private void doDelete(){
        if (adapter.ismultiMode){
            for (Map.Entry<Long,Boolean> entry : recordCursorIdStatus.entrySet()){
                if (entry.getValue()){
                    noteDb.delete(entry.getKey()+"");
                }
            }
            Cursor cursor = noteDb.queryAll();
            adapter.changeCursor(cursor);
            adapter.ismultiMode = false;
            adapter.notifyDataSetChanged();
            mLinearLayout.setVisibility(View.GONE);
            note_new.setVisibility(View.VISIBLE);
            count = 0;
            recordCursorIdStatus.clear();
            recordStatus.clear();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cursor = noteDb.queryAll();
        adapter.changeCursor(cursor);
        switch (requestCode){
            case 100:                  //新增便签返回G列表时
                adapter.notifyDataSetChanged();
                break;
            case 101:                  //修改便签返回列表时
                adapter.notifyDataSetChanged();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (adapter.ismultiMode){
          cancelAction();
        }else if (isHomeFlag == 2){
            Cursor cursor = noteDb.queryAll();
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
            isHomeFlag = 1;
        }else {
            moveTaskToBack(true);
        }


    }

    private void cancelAction(){
        mLinearLayout.setVisibility(View.GONE);
        note_new.setVisibility(View.VISIBLE);
        adapter.ismultiMode = false;
        adapter.choseAll = false;
        adapter.notifyDataSetChanged();
        recordCursorIdStatus.clear();
        recordStatus.clear();
        count = 0;
        delete.setText("删除("+count+")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }else*/ if (id == R.id.action_search){
            if(searchEdit.getVisibility()== View.GONE) {
                searchEdit.setVisibility(View.VISIBLE);
                note_new.setVisibility(View.GONE);
            }else {
                searchEdit.setVisibility(View.GONE);
                note_new.setVisibility(View.VISIBLE);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            // Handle the camera action
            doRefresh("全部");
        } else if (id == R.id.nav_work) {
            Cursor cursor = noteDb.queryWorkClass();
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
            toolbar.setTitle("工作");
           /* toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("工作");
            setSupportActionBar(toolbar);*/
        } else if (id == R.id.nav_life) {
            Cursor cursor = noteDb.queryLifeClass();
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
            toolbar.setTitle("生活");
        } else if (id == R.id.nav_other) {
            Cursor cursor = noteDb.queryOtherClass();
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
            toolbar.setTitle("其他");

        } else if (id == R.id.nav_share) {     //夜间模式
            SharedPreferences spf = getSharedPreferences("config",MODE_PRIVATE);
            boolean isNightMode = spf.getBoolean("theme_value",false);
            if (isNightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                spf.edit().putBoolean("theme_value",false).commit();
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                spf.edit().putBoolean("theme_value",true).commit();
            }
            recreate();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doRefresh(String title){
        Cursor cursor = noteDb.queryAll();
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
        toolbar.setTitle(title);
    }
}
