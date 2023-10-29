package com.example.fundit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Founder_Info extends AppCompatActivity {
    Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founder_info);

        btn_save =(Button) findViewById(R.id.btn_save);



    }

    public void saveFounderDetail()
    {

    }
}