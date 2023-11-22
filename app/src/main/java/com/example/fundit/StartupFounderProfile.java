package com.example.fundit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class StartupFounderProfile extends AppCompatActivity {
    Button profilebtn;
    TextView txt, uname, uemail;
    DBHelper DB;
    SharedPreferences sp;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_founder_profile);

        profilebtn = (Button) findViewById(R.id.profileBtn);
        txt = findViewById(R.id.textView5);
        uname = findViewById(R.id.userName);
        uemail = findViewById(R.id.userEmail);



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

//        DB = new DBHelper(getApplicationContext());
//        sp=getSharedPreferences("session",MODE_PRIVATE);
//        int userID=sp.getInt("userID",0);
//
//        String userIDString = String.valueOf(userID);
//        txt.setText(userIDString);

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                uname.setText(documentSnapshot.getString("firstName") + " " + documentSnapshot.getString("lastName"));
                uemail.setText(documentSnapshot.getString("email"));
            }
        });


        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Founder_Info.class);
                startActivity(intent);
            }
        });
    }

    public void backToDashboard(View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }


}