package com.example.zhl.notedemo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.ShareActionProvider;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhl.notedemo.R;
import com.example.zhl.notedemo.db.NoteDb;
import com.example.zhl.notedemo.utils.NoteUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhl on 2015/12/31.
 */
public class EditNoteActivity extends AppCompatActivity {

    private TextView date;
    private EditText title,content;
    private Toolbar mToolbar;
    private NoteDb noteDb;
    private String starttempdate,starttempcontent,starttemptitle,starttempclass;
    private ShareActionProvider mShareActionProvider;
    private Button note_class;
    private ListView noteClassListView;
    private InputMethodManager imm;
    public static final String[] listClass = {"全部","工作","生活","其他"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        date = (TextView) findViewById(R.id.date);
        note_class = (Button) findViewById(R.id.note_class);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("编辑");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        starttempdate = intent.getStringExtra("editdate");
        if (starttempdate != null){
            starttemptitle = intent.getStringExtra("edittitle");
            starttempcontent = intent.getStringExtra("editcontent");
            starttempclass = intent.getStringExtra("editclass");
            title.setText(starttemptitle);
            content.setText(starttempcontent);
            date.setText(starttempdate);
            note_class.setText(starttempclass);

        }

        note_class = (Button) findViewById(R.id.note_class);
        noteClassListView = (ListView) findViewById(R.id.list_class);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditNoteActivity.this,android.R.layout.simple_list_item_1,listClass);
        noteClassListView.setAdapter(adapter);
        note_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteClassListView.setVisibility(View.VISIBLE);
            }
        });

        content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                if (!hasFocus) {
                    content.setFocusableInTouchMode(false);
                }else {
                    content.setFocusableInTouchMode(true);
                }
            }

        });
        noteClassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String select = listClass[position];
                note_class.setText(select);
                noteClassListView.setVisibility(View.GONE);
            }
        });

        title.setCursorVisible(false);
        title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    title.setFocusableInTouchMode(true);
                    title.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                content.setFocusableInTouchMode(true);
            }
                return false;
            }
        });

        if(starttempcontent!=null){
            content.setFocusableInTouchMode(false);
            title.setFocusableInTouchMode(false);
        }else {
            content.requestFocus();
        }
    }


    @Override
    public void onBackPressed() {

        if (noteClassListView.getVisibility() == View.VISIBLE){
            noteClassListView.setVisibility(View.INVISIBLE);
        }else {
            super.onBackPressed();
            String tempTitle = title.getText().toString();
            String tempContent = content.getText().toString();
            String tempClass = note_class.getText().toString();
            String tempDate = NoteUtil.getDate();
            noteDb = NoteDb.getInstance(this);

            if (starttempdate == null){

                noteDb.saveNote(tempTitle,tempContent,tempDate,tempClass);
            }else if (tempTitle == starttemptitle&&tempContent == starttempcontent){
                return;
            }else if (starttemptitle.equals(tempTitle)&&starttempcontent.equals(tempContent)&&starttempclass.equals(tempClass)){
                return;
            }else {
                noteDb.updateNote(tempTitle, tempContent, tempDate,starttempdate,tempClass);
            }
        }



/*        if (tempContent.equals("")&&tempTitle.equals("")){
            return;
            // Toast.makeText(this,"标题和内容都为空",Toast.LENGTH_SHORT).show();
        }else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String tempDate = dateFormat.format(date);
            noteDb = NoteDb.getInstance(this);
            noteDb.saveNote(tempTitle,tempContent,tempDate);

        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu resource file
        getMenuInflater().inflate(R.menu.edit,menu);

        //Locate MenuItem with ShareActionProvider
        MenuItem itemShare = menu.findItem(R.id.action_share);

        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(itemShare);


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       if (id == R.id.action_share){
            Intent myShareIntent = new Intent(Intent.ACTION_SEND);
            myShareIntent.setType("text/plain");
            String shareContent = content.getText().toString();
            String shareTitle = title.getText().toString();
            if(!shareContent.contains(shareTitle)){
                shareContent = "标题："+shareTitle+"\n"+"内容："+shareContent;
            }
            myShareIntent.putExtra(Intent.EXTRA_TEXT,shareContent);
            startActivity(Intent.createChooser(myShareIntent, getResources().getText(R.string.action_share_to)));

        }
        return super.onOptionsItemSelected(item);
    }
}
