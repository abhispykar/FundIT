package com.example.fundit;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Founder_Info extends AppCompatActivity {
    TextView fID;
    EditText bio,education,experience;
    Button btn_profile;
    SharedPreferences sp;

    ImageView profileImage;

    DBFounder DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founder_info);

        btn_profile =(Button) findViewById(R.id.btn_profile_img);
        bio=findViewById(R.id.bio);
        education=findViewById(R.id.education);
        experience=findViewById(R.id.experience);
        profileImage=findViewById(R.id.profileImg);

        fID=findViewById(R.id.founderID);

        SessionManager sessionManager=new SessionManager((getApplicationContext()));

        DB=new DBFounder(getApplicationContext());

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Open gallery
                Intent openGalleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);

//                sp=getSharedPreferences("session",MODE_PRIVATE);
//                int userID=sp.getInt("userID",0);
//
//                String founder_bio=bio.getText().toString();
//                String founder_education=education.getText().toString();
//                String founder_experience=experience.getText().toString();
//
//                Boolean insert=DB.insertFounderData(founder_education,founder_experience,founder_bio,userID);
//
//                if(insert==true)
//                {
//                    Toast.makeText(getApplicationContext(),"Founder information added successfully",Toast.LENGTH_LONG).show();
//                    int founderID=DB.getFounderID();
//
//                    //Creating object of sharedPreferences
//                    sessionManager.addFounderID((founderID));
//
//
//                    fID.setText(founderID);
//                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);
            }
        }
    }
    //    public void saveFounderDetails(View view)
//    {
//
//
//    }
}