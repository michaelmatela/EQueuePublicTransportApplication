package com.example.phmima.equeue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.core.Context;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth fireBaseAuth;
    private ProgressDialog progressDialog;

    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etPassword;
    EditText etRePassword;
    Spinner spTerminal;

    String firstName;
    String lastName;
    String email;
    String password;
    String rePassword;
    String terminal;
    String errorText;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    ArrayAdapter<String> adapter;

    int appType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appType = Config.APP_TYPE;

        if (appType == 1) {
            setContentView(R.layout.barker_register);
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = mFirebaseDatabase.getReference().child("TerminalList");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    ArrayList<String> sample = new ArrayList<String>();
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        sample.add(ds.getValue().toString());
                    }
                    fillSpinner(sample);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else
            setContentView(R.layout.passenger_register);

        fireBaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        Firebase.setAndroidContext(this);

        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    private void fillSpinner(ArrayList<String> list){
        spTerminal = (Spinner)findViewById(R.id.spTerminal);
        adapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTerminal.setAdapter(adapter);
    }

    private String initialValueCheck() {
        String result = "";
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || rePassword.isEmpty())
            result = getString(R.string.fill_required_fields);
        else if (!password.equals(rePassword))
            result = getString(R.string.password_not_equal);

        return result;
    }

    private void commonWidgetInit() {
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);

        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        rePassword = etRePassword.getText().toString();
    }

    private void barkerStartUp() {
        commonWidgetInit();
        terminal = spTerminal.getSelectedItem().toString();



    }

    private void passengerStartUp() {
        commonWidgetInit();
    }

    private void registerUser(){
        if (appType == 1)
            barkerStartUp();
        else
            passengerStartUp();
        errorText = initialValueCheck();

        if (!errorText.isEmpty()) {
            Toast.makeText(getBaseContext(), errorText, Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        fireBaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            //display some message here

                            fireBaseAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {

                                                Firebase ref = new Firebase(Config.FIREBASE_URL);
                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                Profile profile = new Profile();

                                                profile.setFirstName(firstName);
                                                profile.setLastName(lastName);
                                                profile.setEmail(email);
                                                profile.setPassword(password);

                                                if (appType == 1)
                                                    profile.setTerminal(terminal);

                                                ref.child("Profile").child(user.getUid()).setValue(profile);

                                                user.sendEmailVerification()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    FirebaseAuth.getInstance().signOut();
                                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                                    intent.putExtra("register_text", "success");
                                                                    RegisterActivity.this.startActivity(intent);
                                                                }
                                                            }
                                                        });

                                            }
                                            progressDialog.dismiss();
                                        }
                                    });
                            progressDialog.dismiss();
                        }else{
                            //display some message here
                            Toast.makeText(RegisterActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        registerUser();
    }
}
