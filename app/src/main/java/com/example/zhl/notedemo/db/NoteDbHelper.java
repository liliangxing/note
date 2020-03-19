package com.example.zhl.notedemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by zhl on 2015/12/31.
 */
public class NoteDbHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "create table note ("
            +"_id integer primary key autoincrement,"
            +"title text,"
            +"content text,"
            +"date text,"
            +"class text)";

    public NoteDbHelper(Context context,String name,SQLiteDatabase.CursorFactory cursorFactory,int version){
        super(new CustomPathDatabaseContext(context, getDirPath()),name,cursorFactory,version);
    }

    /**
     * 获取db文件在sd卡的路径
     * @return
     */
    private static String getDirPath(){
        //TODO 这里返回存放db的文件夹的绝对路径
        return new File(Environment.getExternalStorageDirectory()+"").getAbsolutePath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);   //创建Note表

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL("alter table note add column class text");
            default:
        }
    }
}
