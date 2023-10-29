package com.example.fundit;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBFounder extends SQLiteOpenHelper {
    public static final String DBNAME="fundit.db";

    private Context context;

    public DBFounder(Context context) {
        super(context, DBNAME, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table founder(founderID INTEGER primary key AUTOINCREMENT, education TEXT, experience TEXT,bio TEXT,userID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int old_version, int new_version) {
        MyDB.execSQL("drop table if exists founder");
        onCreate(MyDB);
    }

    public boolean insertFounderData(String education,String experience,String bio,int userID)
    {
        SQLiteDatabase MyDB=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put("education",education);
        contentValues.put("experience",experience);
        contentValues.put("bio",bio);
        contentValues.put("userID",userID);

        long result=MyDB.insert("founder",null,contentValues);

        if(result==-1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    //Method to get founderID
    public int getFounderID()
    {
        int founderID=-1;


        SharedPreferences sharedPref = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();


        int userID = sharedPref.getInt("userID", -1);
        SQLiteDatabase MyDB = this.getReadableDatabase();

        String query = "SELECT founderID FROM founder WHERE userID = ?";
        String[] selectionArgs = new String[]{Integer.toString(userID)};

        Cursor cursor = MyDB.rawQuery(query, selectionArgs);


        if(cursor.moveToFirst())
        {
            founderID=cursor.getInt(0);
        }
        return founderID;
    }

    public boolean upadteFounderDetails(String education,String experience,String bio,int founderID)
    {
        SQLiteDatabase MyDB=this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();

        contentValues.put("education",education);
        contentValues.put("experience",experience);
        contentValues.put("bio",bio);

        String[] selectionArgs = new String[]{Integer.toString(founderID)};

        int i=MyDB.update("founder",contentValues,"founderID=?",selectionArgs);

        if(i>0)
        {
            return true;
        }
        else
        {
            return false;
        }


    }


}
