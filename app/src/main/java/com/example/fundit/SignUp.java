package com.example.fundit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Button btnSignUp,btnSignIn;
    EditText firstName,lastName,password,email;
    DBHelper DB;
    Spinner sp_user;
    Validation valid;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent=new Intent(getApplicationContext(),Dashboard.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        firstName=findViewById(R.id.et_first_name);
        lastName=findViewById(R.id.et_last_name);
        password=findViewById(R.id.et_password);
        email=findViewById(R.id.et_email);
        btnSignUp=findViewById(R.id.btn_signup);
        btnSignIn=findViewById(R.id.btn_signin);
        sp_user=findViewById(R.id.sp_usertype);

        DB = new DBHelper(getApplicationContext());
        valid=new Validation();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        //Registration
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = firstName.getText().toString();
                String lname = lastName.getText().toString();
                String userEmail = email.getText().toString();
                String pass = password.getText().toString();
                String userType=sp_user.getSelectedItem().toString();

                if(TextUtils.isEmpty(fname))
                {
                    Toast.makeText(getApplicationContext(),"Please enter first name.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(lname))
                {
                    Toast.makeText(getApplicationContext(),"Please enter last name.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(userEmail))
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
                if (!("Founder".equals(userType) || "Investor".equals(userType))) {
                    Toast.makeText(getApplicationContext(), "Please select a valid user type.", Toast.LENGTH_LONG).show();
                    return;
                }


                //Firebase signup method
                mAuth.createUserWithEmailAndPassword(userEmail, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();

                                    //Creating users collection in fireStore database
                                    userID=mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference=fStore.collection("users").document(userID);
                                    Map<String,Object> user=new HashMap<>();
                                    user.put("firstName",fname);
                                    user.put("lastName",lname);
                                    user.put("email",userEmail);
                                    user.put("userType",userType);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(TAG,"onSuccess: user profile created for "+userID);
                                        }
                                    });
                                    Intent intent = new Intent(SignUp.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                    // Log the exact error message
                                    Log.e("FirebaseAuth", "Authentication failed: " + task.getException().getMessage(), task.getException());
                                }
                            }
                        });


//                else
//                {
//
//                    Boolean checkuser=DB.checkusername(userEmail);
//                    if(checkuser==false)
//                    {
//
//
//                        Boolean insert=DB.insertData(fname,lname,userEmail,pass,userType);
//                        if(insert==true)
//                        {
//                            Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();
//                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
//                            startActivity(intent);
//                        }
//                        else
//                        {
//                            Toast.makeText(getApplicationContext(),"Registration failed",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(),"User already exists! please sign in",Toast.LENGTH_LONG).show();
//                    }
//
//                }
            }
        });
    }


}