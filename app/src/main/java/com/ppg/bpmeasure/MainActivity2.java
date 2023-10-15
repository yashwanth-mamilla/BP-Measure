
package com.ppg.bpmeasure;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.ppg.bpmeasure.Fragment.Measure;
import com.ppg.bpmeasure.Login.LoginPage;
import com.ppg.bpmeasure.Math.StaticConfig;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = "MainActivity2";
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser firebaseUser;

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;
    protected TextView tv_name, tv_age, tv_email;

    public UserInfo user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user= (UserInfo) getIntent().getSerializableExtra("UserInfo");
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("BP Measure");
        }

        viewPager = findViewById(R.id.viewpager);

        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);
        tv_name = headerView.findViewById(R.id.bio_name);
        tv_age = headerView.findViewById(R.id.bio_age);
        tv_email = headerView.findViewById(R.id.bio_email);

        tv_age.setText(String.valueOf((user.age.intValue())));
        tv_name.setText(user.name);
        tv_email.setText(user.email);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA,
            }, 7001);
        }

        setupViewPager(viewPager);

        //initFirebase();
    }
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Measure(), "MEASURE FRAGMENT");
        //adapter.addFrag(new InsightFragment(), STR_INSIGHT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity2.this,LoginPage.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, null);
        finish();
    }
}