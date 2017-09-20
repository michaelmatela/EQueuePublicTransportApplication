package com.example.phmima.equeue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BarkerHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barker_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Help");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BarkerHelp.this, MainActivity.class);
        BarkerHelp.this.startActivity(intent);
    }
}
