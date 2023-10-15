package com.ppg.bpmeasure;



import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ppg.bpmeasure.Adapter.ViewPagerAdapter;
import com.ppg.bpmeasure.Details.UserInfo;
import com.ppg.bpmeasure.Login.LoginPage;
import com.ppg.bpmeasure.Login.Register;
import com.ppg.bpmeasure.Math.StaticConfig;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "MainActivity";
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;
    protected TextView tv_name, tv_age, tv_email;
    Button signout_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

//        signout_btn=(Button) findViewById(R.id.signout_btn);
//        signout_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//            }
//        });
        //startActivity(new Intent(this,Register.class));
        //MainActivity.this.finish();
//        UserInfo user=new UserInfo("Srujana Pillarichey","email","password",Double.valueOf(160),Double.valueOf(60),Double.valueOf(21),1);
//        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//        DatabaseReference reference=firebaseDatabase.getReference();
//
//        reference.setValue("Srujana");
//        reference.child("Users").setValue(user);
        initFirebase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        //mAuth.signOut();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;
    }

    private void initTab() {
//        tabLayout = findViewById(R.id.tabs);
//        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorIndivateTab));
          setupViewPager(viewPager);

//        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //adapter.addFrag(new MeasureFragment(), "MEASURE FRAGMENT");
        //adapter.addFrag(new InsightFragment(), STR_INSIGHT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }
    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {

            user = firebaseAuth.getCurrentUser();

            if (user != null) {
                StaticConfig.UID = user.getUid();
                Log.d(TAG,StaticConfig.UID);
                Log.d(TAG,"Inside initfirebase");
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference reference=firebaseDatabase.getReference();
                reference.child("UserInfo").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG,"Inside OnDataChange");
//                        mAuth.signOut();
//                        MainActivity.this.finish();
                            UserInfo userInfo=dataSnapshot.getValue(UserInfo.class);
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.putExtra("UserInfo",userInfo);
                            startActivity(intent);
                            finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {

                // User is signed in
                startActivity(new Intent(MainActivity.this, LoginPage.class));
                MainActivity.this.finish();
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };
    }


}

