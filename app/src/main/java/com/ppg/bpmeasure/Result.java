package com.ppg.bpmeasure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ppg.bpmeasure.Details.UserInfo;

public class Result extends AppCompatActivity {

    UserInfo userInfo;
    private TextView textView,tv_syst,tv_diat,tv_hrt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        userInfo=(UserInfo)getIntent().getSerializableExtra("UserInfo");
        int systole=(int)getIntent().getSerializableExtra("Systole");
        int diastole=(int)getIntent().getSerializableExtra("Diastole");
        int heartRate=(int)getIntent().getSerializableExtra("HeartRate");
        textView=(TextView) findViewById(R.id.text1);
        tv_syst=(TextView)findViewById(R.id.tv_sys);
        tv_diat=(TextView)findViewById(R.id.tv_by);
       // tv_hrt=(TextView)findViewById(R.id.textView4);

        tv_syst.setText(String.valueOf(systole));
        tv_diat.setText(String.valueOf(diastole));
       //x tv_hrt.setText(String.valueOf(heartRate));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity2.class);
        this.finish();
        i.putExtra("UserInfo", userInfo);
        startActivity(i);

    }
}