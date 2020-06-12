package com.example.zhl.notedemo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteUtil {
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    public  static String getDate(){
        Date date = new Date();
        String tempDate = dateFormat.format(date);
        return tempDate;
    }
    public  static String getDateYMD(){
        Date date = new Date();
        String tempDate = dateFormat2.format(date);
        return tempDate;
    }
}
