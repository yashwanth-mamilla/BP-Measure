package com.ppg.bpmeasure.Login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ppg.bpmeasure.Calibration;
import com.ppg.bpmeasure.MainActivity;
import com.ppg.bpmeasure.R;

public class LoginPage extends AppCompatActivity {


    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private EditText email;
    private EditText password;
    private TextView signup;
    private Button login;

    String email_str;
    String pass_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email=findViewById(R.id.email_id);
        password=findViewById(R.id.password);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        signup=findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this, Register.class);
                startActivity(i);
                LoginPage.this.finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_str = email.getText().toString();
                pass_str = password.getText().toString();
                OnCompleteListener<AuthResult> listener= new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(LoginPage.this, MainActivity.class));
                            LoginPage.this.finish();
                        }
                        else{
                            Toast.makeText(LoginPage.this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                if(email_str==null || pass_str==null || email_str.isEmpty() || pass_str.isEmpty())
                {
                    Toast.makeText(LoginPage.this, "Enter the Correct Login Details ", Toast.LENGTH_SHORT).show();
                }
                else
                mAuth.signInWithEmailAndPassword(email_str,pass_str).addOnCompleteListener(listener);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if (user != null) {
            user.getUid();
            Log.d("Login", "onAuthStateChanged:signed_in:" + user.getUid());
            startActivity(new Intent(LoginPage.this, MainActivity.class));
            LoginPage.this.finish();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED, null);
        finish();
    }

}