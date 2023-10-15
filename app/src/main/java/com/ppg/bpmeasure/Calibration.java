package com.ppg.bpmeasure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ppg.bpmeasure.Details.UserInfo;
import com.ppg.bpmeasure.Login.Register;

import java.security.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Calibration extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    EditText ed_systole,ed_diastole,ed_heart_rate;
    Double systole,diastole,heart_rate;
    Button btn_calibrate;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        mAuth = FirebaseAuth.getInstance();
        ed_systole=findViewById(R.id.ed_systole_calib);
        ed_diastole=findViewById(R.id.et_diastole_calib);
        ed_heart_rate=findViewById(R.id.ed_heart_rate);
        userInfo=(UserInfo) getIntent().getSerializableExtra("UserInfo");
        btn_calibrate=findViewById(R.id.btn_calibrate_ok);
        btn_calibrate.setOnClickListener(v -> calibration());

    }

    private void calibration() {
        Log.d("CALIBRATION","IN CALIBRATION PROCESS");
        systole= Double.valueOf(ed_systole.getText().toString());
        diastole= Double.valueOf(ed_diastole.getText().toString());
        heart_rate= Double.valueOf(ed_heart_rate.getText().toString());

        double pulsePressure = (systole - diastole);
        double meanArterialPressure = (diastole + pulsePressure / 3);
        double strokeVolume = pulsePressure * ((0.013 * userInfo.weight - 0.007 * userInfo.age - 0.004 * heart_rate) + 1.307);
        double BSA = 0.007184*(Math.pow(userInfo.height, 0.725))*(Math.pow(userInfo.weight, 0.425));

        double resistance = meanArterialPressure / ((userInfo.gender == UserInfo.MALE) ? 5.0 : 4.5);
        double ET = ((strokeVolume + 6.6 + (0.62 * heart_rate) - (40.4 * BSA) + (0.51 * userInfo.age)) / 0.25 ) + 35;

        userInfo.ejectionTime=ET;
        userInfo.ROB=resistance;
        Log.d("Calibration",ET+" "+resistance);
        if(userInfo.ejectionTime!=0 && userInfo.ejectionTime!=0){
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DATE,30);
            userInfo.timestampDate= calendar.getTime();
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference=database.getReference();
            reference.child("UserInfo").child(user.getUid()).child("ejectionTime").setValue(ET);
            reference.child("UserInfo").child(user.getUid()).child("ROB").setValue(resistance);
            Toast.makeText(Calibration.this, "Calibration successfull", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Calibration.this,MainActivity2.class);
            intent.putExtra("UserInfo",userInfo);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(Calibration.this, "Calibration unsuccessfull", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

}