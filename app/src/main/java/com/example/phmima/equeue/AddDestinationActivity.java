package com.example.phmima.equeue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddDestinationActivity extends AppCompatActivity {

    EditText etDestination;
    String destination;
    String terminal;
    Button btnAdd;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_destination);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(this);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        this.terminal = preferences.getString("terminal", "");

        etDestination = (EditText) findViewById(R.id.etDestination);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        progressDialog = new ProgressDialog(this);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();
                destination = etDestination.getText().toString();
                addDestination();
            }
        });
    }

    private void addDestination(){
        Firebase ref = new Firebase(Config.FIREBASE_URL);
        Destination destination = new Destination();

        destination.setTerminal(this.terminal);
        destination.setDestination(this.destination);
        destination.setQueueCount("0");
        destination.setCurrentCount("0");
        destination.setPhoto("");

        ref.child("Destination").child(this.terminal).child(this.destination).setValue(destination);

        Toast.makeText(getBaseContext(), "Add destination successful.", Toast.LENGTH_LONG).show();

        etDestination.setText("");
        progressDialog.dismiss();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
