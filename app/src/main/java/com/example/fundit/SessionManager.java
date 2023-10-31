package com.example.fundit;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private final String PREF_FILE_NAME="session";
    private final int PRIVATE_MODE=0;

    public SessionManager(Context context)
    {
        this.context=context;

        sp=context.getSharedPreferences(PREF_FILE_NAME,PRIVATE_MODE);
        editor=sp.edit();
    }

    public void createSession(int userID)
    {
        editor.putInt("userID",userID);
        editor.putBoolean("isLoggedIn",true);
        editor.commit();
    }

    public boolean checkLogin()
    {
        if(sp.contains("isLoggedIn"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void addFounderID(int founderID)
    {
        editor.putInt("userID",founderID);
        editor.commit();
    }



}
