package com.example.phmima.equeue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    private String email;
    private String password;
    private String errorText;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        setTitle("Barker Application");
        if (Config.APP_TYPE == 2)
            setTitle("Passenger Application");

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();



        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginInit();
                errorText = validateText();
                if (errorText.isEmpty())
                    login();
            }
        });

        user = mAuth.getCurrentUser();

        if (user != null){
            getAccountData();
        }


        tvRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                register();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (user.isEmailVerified()){
                        getAccountData();
                    }
                    else {

                    }
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
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    if (Config.APP_TYPE == 2){
                        progressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_LONG).show();
                    }
                }
                progressDialog.dismiss();
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

    private String validateText(){
        String result = "";

        if (email.isEmpty() || password.isEmpty())
            result = getString(R.string.fill_email_password);

        return result;
    }

    private void login(){
        progressDialog.setMessage("Signing in Please Wait...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password);
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

    //go to register activity
    private void register(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }

}
