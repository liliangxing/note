package com.example.zhl.notedemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.zhl.notedemo.ui.EditNoteActivity;

/**
 * Created by zhl on 2015/12/31.
 */
public class NoteDb {

    private static NoteDb noteDb;
    private static final String DB_NAME = "note_db.db";
    private static final int VERSION = 2;
    private SQLiteDatabase db;


    private NoteDb(Context context){
        NoteDbHelper noteDbHelper = new NoteDbHelper(context,DB_NAME,null,VERSION);
        db = noteDbHelper.getWritableDatabase();
    }

    public synchronized static NoteDb getInstance(Context context){
        if (noteDb == null){
            noteDb = new NoteDb(context);
        }
        return noteDb;
    }

    //新增便签
    public long saveNote(String tempTitle,String tempContent,String tempDate,String tempClass){
        if(TextUtils.isEmpty(tempTitle+tempContent)) return 0;
        ContentValues values = new ContentValues();
        values.put("title",tempTitle);
        values.put("content",tempContent);
        values.put("date",tempDate);
        values.put("class",tempClass);
        if(TextUtils.isEmpty(tempTitle)){
            values.put("title",tempContent.trim().substring(0,16>tempContent.length()?tempContent.length():16));
        }
        long id = db.insert("note", null, values);
        values.clear();
        return id;
    }

    //更新便签
    public int updateNote(String tempTitle, String tempContent, String editId,String tempClass){
        ContentValues values = new ContentValues();
        values.put("title",tempTitle);
        values.put("content",tempContent);
        values.put("class", tempClass);
        return db.update("note", values, "_id = ?", new String[]{editId});
    }

    //更新便签
    public void updateNote(String tempDate, String editId){
        ContentValues values = new ContentValues();
        values.put("date", tempDate);
        db.update("note", values, "_id = ?", new String[]{editId});
    }

    //查询所有数据库数据（all）
    public Cursor queryAll(){
        return db.query("note",null,null,null,null,null,"date desc");
    }

     public Cursor queryByClass(String classname){
        if(classname.equals(EditNoteActivity.listClass[0])){
            return queryAll();
        }
        return db.query("note",null,"class = ?",new String[]{classname},null,null,null);
    }
    public Cursor queryByTitle(String keyword){
        return db.query("note",null,"title = ?",new String[]{keyword},null,null,"date desc");
    }
    //查询所有数据库数据（work）
    public Cursor queryWorkClass(){
        return db.query("note",null,"class = ?",new String[]{"工作"},null,null,null);
    }

    //查询所有数据库数据（life）
    public Cursor queryLifeClass(){
        return db.query("note",null,"class = ?",new String[]{"生活"},null,null,null);
    }

    //查询所有数据库数据（other）
    public Cursor queryOtherClass(){
        return db.query("note",null,"class = ?",new String[]{"其他"},null,null,null);
    }


    //删除选中的数据
    public void delete(String cursorId){
        db.delete("note","_id = ?",new String[]{cursorId});
    }


    //根据关键字查询数据库中Cursor
    public Cursor search(String Categories){
        String sql_ser = "select * from note where title like '%"+Categories+"%' or content like '%"+Categories+"%' order by date desc" ;
        return db.rawQuery(sql_ser,null);
    }


    //由于升级数据库，更新所有未设置分类的记录，统一更新为全部分类
    public void updateDefaultClass(){

        db.execSQL("update note set class = ? where class is ?",new String[]{"全部",null});
    }
    public void updateContentById(String content,String tempDate,String id){
        db.execSQL("update note set content =  ? , date = ? where _id = ?",
                new String[]{content,tempDate,id});
    }

    public void updateContentById(String title,String content,String tempDate,String id){
        db.execSQL("update note set title =  ? ,content =  ? , date = ? where _id = ?",
                new String[]{title,content,tempDate,id});
    }



}
