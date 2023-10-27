package com.example.fundit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME="Login.db";

    public DBHelper(Context context){
        super(context,"Login.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB)
    {
        MyDB.execSQL("create Table users(userID INTEGER primary key AUTOINCREMENT,firstNamepassword TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");

    }
    public Boolean insertData(String fname,String lname,String email,String password,String userType){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("firstName",fname);
        contentValues.put("lastName",lname);
        contentValues.put("username",email);
        contentValues.put("password",password);
        contentValues.put("userType",userType);
        long result = MyDB.insert("users",null,contentValues);
        if(result==-1) return false;
        else
            return true;
    }
    public Boolean checkusername(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from user where username=?",new String[]{email});
        if(cursor.getCount()>0)
            return  true;
        else
            return false;

    }
    public boolean checkusernamepassword(String username,String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor= MyDB.rawQuery("Select * from users where username=? and password = ? ",new String[]{username,password});
        if(cursor.getCount()>0)
            return true;
        else
             return false;


    }
}
