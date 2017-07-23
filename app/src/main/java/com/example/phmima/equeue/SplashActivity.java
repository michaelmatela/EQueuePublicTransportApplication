package com.example.phmima.equeue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private String email;
    private String password;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (user.isEmailVerified()){
                        getAccountData();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        SplashActivity.this.startActivity(intent);

                    }
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(intent);

                }
            }
        };
    }

    private void getAccountData(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Profile").child(user.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("terminal")) {
                    if (Config.APP_TYPE == 1){
                        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("terminal", snapshot.child("terminal").getValue().toString());
                        editor.commit();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(intent);
                    }
                    else{
                        Toast.makeText(SplashActivity.this,"Login failed",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if (Config.APP_TYPE == 2){
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(intent);
                    }
                    else{
                        Toast.makeText(SplashActivity.this,"Login failed",Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loginInit(){
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
