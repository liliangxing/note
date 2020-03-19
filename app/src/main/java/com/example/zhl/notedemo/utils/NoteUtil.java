package com.example.zhl.notedemo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteUtil {
    public  static String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String tempDate = dateFormat.format(date);
        return tempDate;
    }
}
