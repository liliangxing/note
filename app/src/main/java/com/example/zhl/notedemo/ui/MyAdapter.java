package com.example.zhl.notedemo.ui;

import android.content.Context;
import android.database.Cursor;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhl.notedemo.R;

/**
 * Created by zhl on 2015/12/31.
 */
public class MyAdapter extends CursorAdapter {

    public Boolean ismultiMode = false;
    public Boolean choseAll = false;


    public MyAdapter(Context context,Cursor cursor){
        super(context, cursor, true);
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        return cursor.getLong(cursor.getColumnIndex("_id"));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        viewHolder.title_item = (TextView) view.findViewById(R.id.title_item);
        viewHolder.date_item = (TextView) view.findViewById(R.id.date_item);
        viewHolder.cb_item = (CheckBox) view.findViewById(R.id.check);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String temptitle_item = cursor.getString(cursor.getColumnIndex("title"));
        String tempdate_item = cursor.getString(cursor.getColumnIndex("date"));

       if (ismultiMode){
             viewHolder.cb_item.setVisibility(View.VISIBLE);
           if (choseAll){
               viewHolder.cb_item.setChecked(true);
           }else {
               if (MainActivity.recordStatus.isEmpty()){
                   viewHolder.cb_item.setChecked(false);
               }else{
                   Toast.makeText(context,"数据异常，请查看hashmap的数据！",Toast.LENGTH_LONG).show();
               }
           }

             //Boolean flag = MainActivity.recordStatus.get(getItemId());
         }else {
            viewHolder.cb_item.setVisibility(View.GONE);
         }

        viewHolder.title_item.setText(temptitle_item);
        viewHolder.date_item.setText(tempdate_item);

    }

    class ViewHolder{
        TextView title_item;
        TextView date_item;
        CheckBox cb_item;
    }

}
