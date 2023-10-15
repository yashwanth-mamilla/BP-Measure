package com.ppg.bpmeasure.Login;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ppg.bpmeasure.Calibration;
import com.ppg.bpmeasure.Details.UserInfo;
import com.ppg.bpmeasure.MainActivity;
import com.ppg.bpmeasure.MainActivity2;
import com.ppg.bpmeasure.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Register extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth mAuth;
    DatePickerDialog.OnDateSetListener date;

    EditText et_name, et_email, et_password, et_height,et_weight;
    TextView tv_age;
    RadioGroup radioGroup;
    RadioButton radioButton;

    private String name, email, password;
    private Double height,weight,age;
    private int gender;

    Calendar myCalendar;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);

        radioGroup = findViewById(R.id.radioGroup);
        register = findViewById(R.id.btn_register);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        tv_age.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(Register.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });

        //fun();

    }

    private void fun() {

        et_name.setText("Srujana Pillarichey");
        et_email.setText("pillu@bebetom.com");
        et_height.setText("170");
        et_weight.setText("54");
        radioGroup.check(R.id.male);
        et_password.setText("123456");

    }

    private void doRegister() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        if (radioButton.getText().equals("Male")) {
            gender = UserInfo.MALE;

        }else if (radioButton.getText().equals("Female")){
            gender = UserInfo.FEMALE;
        }
        name = et_name.getText().toString();
        email = et_email.getText().toString();
        password = et_password.getText().toString();

        age = getAges(myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        height = Double.valueOf(et_height.getText().toString());
        weight = Double.valueOf(et_weight.getText().toString());

        if(name==null || email == null || password == null || age==null || height == null || weight == null){
            Toast.makeText(Register.this, "Enter All The Details", Toast.LENGTH_SHORT).show();
            return ;
        }

        Log.d("Register", email + password);

        OnCompleteListener onCompleteListener= task -> {
            if (task.isSuccessful()) {
                user = mAuth.getCurrentUser();

                saveUser();
                Log.d("REGISTER ACTIVITY","DATA SAVED");
                Toast.makeText(Register.this, "Register and Login success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this, MainActivity.class));
                Register.this.finish();

            } else {
                Log.d("REGISTER ACTIVITY","DATA NOT SAVED");
                Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }

        };
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);

        UserInfo user=new UserInfo(name,email,password,height,weight,age,gender);
        Intent i=new Intent(Register.this,MainActivity.class);
        i.putExtra("UserInfo",user);
        startActivity(i);
        Register.this.finish();

    }


    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

    private Double getAges(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Double ages = Double.valueOf(age);
        return ages;
    }

    private void saveUser() {
        UserInfo newUser = new UserInfo(name,email,password,height,weight,age,gender);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference();
        reference.child("UserInfo").child(user.getUid()).setValue(newUser);
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);

        tv_age.setText(simpleDateFormat.format(myCalendar.getTime()));
    }


}