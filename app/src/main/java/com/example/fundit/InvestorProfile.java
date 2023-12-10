package com.example.fundit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class InvestorProfile extends AppCompatActivity {
    Button profilebtn;
    TextView txt, uname, uemail, userbio,userexperience,usereducation;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    StorageReference storageReference;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_profile);

        profilebtn = (Button) findViewById(R.id.profileBtn);

        txt = findViewById(R.id.textView5);
        uname = findViewById(R.id.userName);
        uemail = findViewById(R.id.userEmail);
        userbio = findViewById(R.id.investorbio);
        userexperience = findViewById(R.id.experience);
        usereducation = findViewById(R.id.education);

        profileImage=findViewById(R.id.profileImgHome);



        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        storageReference= FirebaseStorage.getInstance().getReference();
        StorageReference profileRef=storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });


        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");
                    String email = documentSnapshot.getString("email");
                    String userbioValue = documentSnapshot.getString("userbio");
                    String experienceValue = documentSnapshot.getString("experience");
                    String educationValue = documentSnapshot.getString("education");

                    // Set default message if the value is null
                    String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
                    uname.setText(!fullName.trim().isEmpty() ? fullName : "Details not available");
                    uemail.setText(email != null ? email : "Details not available");
                    userbio.setText(userbioValue != null ? userbioValue : "Details not available");
                    userexperience.setText(experienceValue != null ? experienceValue : "Details not available");
                    usereducation.setText(educationValue != null ? educationValue : "Details not available");
                }
            }
        });

        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Investor_Info.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        StorageReference profileRef=storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

    }

    public void backToDashboard(View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}