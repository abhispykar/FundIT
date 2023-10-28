package com.example.fundit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button btnlogin,btnsignup;

    DBHelper DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        btnlogin=(Button) findViewById(R.id.btn_signin);
        btnsignup=(Button) findViewById(R.id.btnsignup);
        DB = new DBHelper(getApplicationContext());



        // Signup
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,Dashboard.class);
                startActivity(intent);

            }
        });






        //Login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user =email.getText().toString();
                String pass=password.getText().toString();

                if(user.equals("")||pass.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter all details.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Boolean checkuserpass=DB.checkusernamepassword(user,pass);
                    if(checkuserpass==true)
                    {
                        Toast.makeText(getApplicationContext(),"Sign in Successfully",Toast.LENGTH_LONG).show();

                        String userType=DB.userTypeCheck(user);
                        if(userType=="Investor")
                        {
                            Intent intent=new Intent(getApplicationContext(),InvestorProfile.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent=new Intent(getApplicationContext(),StartupFounderProfile.class);
                            startActivity(intent);
                        }

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }
}