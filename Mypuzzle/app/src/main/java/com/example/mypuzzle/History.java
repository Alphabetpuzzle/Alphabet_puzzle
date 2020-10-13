package com.example.mypuzzle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class History extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_contacts;
    private TextView tv_title;

    private MyDbHElper myDbHElper;
    private SQLiteDatabase db;

    private ContentValues values;
    private  static final String mTableName = "contacts";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        queryData();
    }

    private void initView() {

        tv_contacts=findViewById(R.id.tv_contacts);

        myDbHElper =new MyDbHElper(this);


    }

    @Override
    public void onClick(View v) {
    }

    private void shoWMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void addData(String uname,String stepcount,String gamedate)
    {
        db=myDbHElper.getWritableDatabase();
        values = new ContentValues();
        values.put("uname",uname);
        values.put("stepcount",stepcount);
        values.put("gamedate",gamedate);

        db.insert(mTableName,null,values);
        shoWMsg("成功添加");
        //queryData();
    }
    private void queryData()
    {
        db = myDbHElper.getReadableDatabase();
        Cursor cousor =db.query(mTableName,null,null,null,null,null,null);
        if(cousor.getCount()==0)
        {
            tv_title.setText("还没开始玩哦，还没有记录");
        }
        else {
            String strresult="";
            while(cousor.moveToNext())
            {
                strresult +=cousor.getInt(0);
                strresult +="    时间："+cousor.getString(1)+"秒     ";
                strresult +="步数："+cousor.getString(2)+"步     ";
                strresult +="日期："+cousor.getString(3);
                strresult +="\n";
            }
            tv_contacts.setText(strresult);


        }
    }
    static class MyDbHElper extends SQLiteOpenHelper {

        public MyDbHElper(@Nullable Context context) {
            super(context, "mycontacts.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table "+mTableName+"(_id integer primary key autoincrement,"+"uname varchar(50) unique,stepcount varchar(10),gamedate varchar(10))");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}