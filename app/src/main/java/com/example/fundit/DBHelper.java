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
        MyDB.execSQL("create Table users(userID INTEGER primary key AUTOINCREMENT,firstName TEXT,lastName TEXT,email TEXT,password TEXT,userType TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int old_version, int new_version) {
        MyDB.execSQL("drop Table if exists users");
        onCreate(MyDB);

    }

    // Method to insert data into database
    public Boolean insertData(String fname,String lname,String email,String password,String userType){
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues= new ContentValues();
        contentValues.put("firstName",fname);
        contentValues.put("lastName",lname);
        contentValues.put("email",email);
        contentValues.put("password",password);
        contentValues.put("userType",userType);

        long result = MyDB.insert("users",null,contentValues);
//        MyDB.close();

        if(result==-1) return false;
        else
            return true;
    }

    //method to check for existing user
    public Boolean checkusername(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email=?",new String[]{email});
        if(cursor.getCount()>0)
            return  true;
        else
            return false;
    }

    //method to validate user's email and password in database
    public boolean checkusernamepassword(String email,String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor= MyDB.rawQuery("Select * from users where email=? and password = ? ",new String[]{email,password});
        if(cursor.getCount()>0)
            return true;
        else
             return false;
    }

    //method to check type of user
    public String userTypeCheck(String email)
    {
        String userType="";
        SQLiteDatabase MyDB=this.getReadableDatabase();
        String query="Select userType from users where email=?";
        Cursor cursor=MyDB.rawQuery(query,new String[]{email});

        if(cursor.moveToFirst())
        {
            userType=cursor.getString(0);
        }
        return userType;
    }

}
