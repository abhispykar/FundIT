package com.example.fundit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartupFounderProfile extends AppCompatActivity {
    Button btn;
    TextView txt;
    DBHelper DB;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_founder_profile);

        btn=(Button)findViewById(R.id.button3);
        txt=findViewById(R.id.textView5);

        DB = new DBHelper(getApplicationContext());
        sp=getSharedPreferences("session",MODE_PRIVATE);
        int userID=sp.getInt("userID",0);

        String userIDString = String.valueOf(userID);
//        txt.setText(userIDString);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(),Founder_Info.class);
                startActivity(intent);
            }
        });
    }


}