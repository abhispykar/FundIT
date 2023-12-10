package com.example.fundit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Investor_Info extends AppCompatActivity {

    TextView userName,userEmail;
    EditText bio,education,experience;
    Button btn_profile,updatebtn;

    FirebaseAuth fAuth;
    StorageReference storageReference;



    FirebaseFirestore fStore;

    ImageView profileImage;
    String userID;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor_info);

        btn_profile =(Button) findViewById(R.id.btn_profile_img);
        updatebtn = (Button) findViewById(R.id.btn_update);

        userName=findViewById(R.id.username);
        userEmail=findViewById(R.id.emailid);
        bio=findViewById(R.id.bio);
        education=findViewById(R.id.education);
        experience=findViewById(R.id.experience);
        profileImage=findViewById(R.id.profileImg);

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


        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Open gallery
                Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    uploadImageToFirebase(imageUri);
                }
                updateUserDetails();
                Toast.makeText(getApplicationContext(), "Details Updated Successfully", Toast.LENGTH_LONG).show();
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
                    userName.setText(!fullName.trim().isEmpty() ? fullName : "Details not available");
                    userEmail.setText(email != null ? email : "Details not available");
                    bio.setText(userbioValue != null ? userbioValue : "");
                    experience.setText(experienceValue != null ? experienceValue : "");
                    education.setText(educationValue != null ? educationValue : "");

                }
            }
        });
    }

    private void updateUserDetails() {
        // updating details in firestore storage
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);

        // Get the text from EditText fields
        String bioText = bio.getText().toString();
        String experienceText = experience.getText().toString();
        String educationText = education.getText().toString();

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("userbio", bioText);
        userDetails.put("experience", experienceText);
        userDetails.put("education", educationText);

        documentReference.update(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: user details updated for " + userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: Error updating user details", e);
                Toast.makeText(getApplicationContext(), "Failed to update details", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                imageUri = data.getData();
                // profileImage.setImageURI(imageUri);
            }
        }
    }


    private void uploadImageToFirebase(Uri imageUri) {

        //upload image to firebase storage
        StorageReference fileRef=storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profileImage);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image upload Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

}