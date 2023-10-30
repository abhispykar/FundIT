package com.example.fundit;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Founder_Info extends AppCompatActivity {
    TextView fID;
    EditText bio,education,experience;
    Button btn_save;
    SharedPreferences sp;

    DBFounder DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founder_info);

        btn_save =(Button) findViewById(R.id.btn_save);
        bio=findViewById(R.id.bio);
        education=findViewById(R.id.education);
        experience=findViewById(R.id.experience);

        fID=findViewById(R.id.founderID);

        DB=new DBFounder(getApplicationContext());


    }

    public void saveFounderDetails(View view)
    {
        sp=this.getSharedPreferences("session",MODE_PRIVATE);
        int userID=sp.getInt("userID",-1);

        String founder_bio=bio.getText().toString();
        String founder_education=education.getText().toString();
        String founder_experience=experience.getText().toString();

        Boolean insert=DB.insertFounderData(founder_education,founder_experience,founder_bio,userID);

        if(insert==true)
        {
            Toast.makeText(getApplicationContext(),"Founder information added successfully",Toast.LENGTH_LONG).show();
            int founderID=DB.getFounderID();

            //Creating object of sharedPreferences
            sp=getApplicationContext().getSharedPreferences("session",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putInt("founderID",founderID);

            fID.setText(founderID);
        }


    }
}