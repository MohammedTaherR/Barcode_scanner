package com.example.zeroq;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class dbhandler extends SQLiteOpenHelper {
    public dbhandler(@Nullable Context context) {
        super(context, "DB_NAME", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String mytable = " CREATE TABLE productsList( codeNumber INTEGER ,name TEXT ,prices INTEGER ); ";
        Log.d("table", mytable);
        db.execSQL(mytable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Boolean addlist(String codeNumber, String name, String prices) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codeNumber", codeNumber);
        values.put("name", name);
        values.put("prices", prices);
        Long res=  db.insert("productsList",null,values);
        if(res==-1) {
            return false;
        }else {
            return true;
        }
    }

    public void delete (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from  productsList");
        db.close();
    }
    public Cursor getdata (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM productsList" ,null);
        return  cursor;

    }
//    public  Cursor get_amount(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor= db.rawQuery("SELECT prices FROM prductsList",null,null);
//        return cursor;
//
//    }

}
