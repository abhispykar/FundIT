package com.example.fundit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button btnlogin,btnsignup;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    DBHelper DB;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser != null){

            Intent intent=new Intent(getApplicationContext(),Dashboard.class);
            startActivity(intent);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        fAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();


        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        btnlogin=(Button) findViewById(R.id.btn_signin);
        btnsignup=(Button) findViewById(R.id.btnsignup);
        DB = new DBHelper(getApplicationContext());

        SessionManager sessionManager=new SessionManager((getApplicationContext()));

        // Signup
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);

            }
        });






        //Login
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user =email.getText().toString();
                String pass=password.getText().toString();



                if(TextUtils.isEmpty(user))
                {
                    Toast.makeText(getApplicationContext(),"Please enter email.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(getApplicationContext(),"Please enter password.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(pass.length()<6)
                {
                    password.setError("Password must contain 6 characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                                    startActivity(intent);

                                    FirebaseUser user = fAuth.getCurrentUser();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });















//                else
//                {
//                    Boolean checkuserpass=DB.checkusernamepassword(user,pass);
//                    if(checkuserpass==true)
//                    {
//
//                        Cursor cursor;
//                        //Setting user Details for session
//                        cursor=DB.getUserDetails(user);
//                        if(cursor.moveToFirst())
//                        {
//                            userID=cursor.getInt(0);
//                        }
//
//
//
//
//                        sessionManager.createSession(userID);
//
//
//                        Toast.makeText(getApplicationContext(),"Sign in Successfully",Toast.LENGTH_LONG).show();
//
//                        String userType=DB.userTypeCheck(user);
//                        if(userType=="Investor")
//                        {
//                            Intent intent=new Intent(getApplicationContext(),InvestorProfile.class);
//                            startActivity(intent);
//                        }
//                        else
//                        {
//                            Intent intent=new Intent(getApplicationContext(),Dashboard.class);
//                            startActivity(intent);
//                        }
//
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
//                    }
//
//                }
            }
        });

    }
}