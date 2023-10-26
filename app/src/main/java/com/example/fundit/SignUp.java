package com.example.fundit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    Button btnSignUp;
    EditText firstName,lastName,password,email;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName=findViewById(R.id.et_first_name);
        lastName=findViewById(R.id.et_last_name);
        password=findViewById(R.id.et_password);
        email=findViewById(R.id.et_email);

        btnSignUp=findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = firstName.getText().toString();
                String lname = lastName.getText().toString();
                String userEmail = email.getText().toString();
                String pass = password.getText().toString();

                if(fname.equals("")||lname.equals("")||userEmail.equals("")||pass.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter all details.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Boolean checkuser=DB.checkusername(userEmail);
                    if(checkuser==false)
                    {
                        Boolean insert=DB.insertData(fname,lname,userEmail,pass);
                        if(insert==true)
                        {
                            Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Registration failed",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"User already exists! please sign in",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }


}