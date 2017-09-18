package com.example.phmima.equeue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PassengerHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Help");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PassengerHelp.this, MainActivity.class);
        PassengerHelp.this.startActivity(intent);
    }
}
