package com.example.fundit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.example.fundit.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID,userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationview);
        toolbar = findViewById(R.id.toolbar);
        navigationView.bringToFront();

        fAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userID=fAuth.getCurrentUser().getUid();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Dashboard.this, drawerLayout, toolbar, R.string.naviopen, R.string.naviclose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        initSlider();
        getOverflowMenu();

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.sidemenu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        int id=item.getItemId();
//
//        if(id==R.id.account)
//        {
//            Intent intent=new Intent(Dashboard.this,StartupFounderProfile.class);
//            startActivity(intent);
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            @SuppressLint("SoonBlockedPrivateApi") Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initSlider() {
        // Java
        ImageCarousel carousel = findViewById(R.id.carousel);

// Register lifecycle. For activity this will be lifecycle/getLifecycle() and for fragments it will be viewLifecycleOwner/getViewLifecycleOwner().
        carousel.registerLifecycle(getLifecycle());

        List<CarouselItem> list = new ArrayList<>();

// Image URL with caption
        list.add(
                new CarouselItem(
                        "https://images.unsplash.com/photo-1532581291347-9c39cf10a73c?w=1080",
                        "Photo by Aaron Wu on Unsplash"
                )
        );
        // Just image URL
        list.add(
                new CarouselItem(
                        "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080"
                )
        );

// Image URL with header_
        Map<String, String> headers = new HashMap<>();
        headers.put("header_key", "header_value");

        list.add(
                new CarouselItem(
                        "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1080",
                        headers
                )
        );

//// Image drawable with caption
//        list.add(
//                new CarouselItem(
//                        R.drawable.fundit_logo_2,
//                        "Photo by Kimiya Oveisi on Unsplash"
//                )
//        );
//
//// Just image drawable
//        list.add(
//                new CarouselItem(
//                        R.drawable.fundit_logo
//                )
//        );
//
//// ...

        carousel.setData(list);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.account:
                // Retrieving user Type
                DocumentReference documentReference = fStore.collection("users").document(userID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Handle the error or log it for debugging
                            Log.e("FirestoreListener", "Error: " + error.getMessage(), error);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            // DocumentSnapshot is not null and exists, you can access its data
                            String userType = documentSnapshot.getString("userType");

                            // Check if userType is not null before using it
                            if (userType != null) {
                                // Use userType here or call a method with userType as a parameter
                                handleUserType(userType);
                            } else {
                                // Handle the case where "userType" is null
                                Log.w("FirestoreListener", "UserType is null for user with ID: " + userID);
                            }
                        } else {
                            // Handle the case where the document doesn't exist
                            Log.w("FirestoreListener", "Document does not exist for user with ID: " + userID);
                        }
                    }
                });


                break;

            case R.id.aboutus:
                Toast.makeText(this,"About Us",Toast.LENGTH_LONG).show();
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent =new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleUserType(String userType) {
        if ("Investor".equals(userType)) {
            Intent intent = new Intent(getApplicationContext(), InvestorProfile.class);
            startActivity(intent);
        } else if ("Founder".equals(userType)) {
            Intent intent = new Intent(getApplicationContext(), StartupFounderProfile.class);
            startActivity(intent);
        }

    }
}