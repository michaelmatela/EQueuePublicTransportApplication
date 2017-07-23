package com.example.phmima.equeue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class AddPUVActivity extends AppCompatActivity {

    EditText etPlateNumber;
    RadioButton rbSeats;
    RadioGroup rgSeats;
    Button btnAdd;

    ProgressDialog progressDialog;

    String plateNumber;
    String terminal;
    String destination;
    String seats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_puv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(this);

        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        this.terminal = preferences.getString("terminal", "");
        Bundle extras = getIntent().getExtras();
        destination = extras.getString("destination");

        btnAdd = (Button) findViewById(R.id.btnAdd);
        progressDialog = new ProgressDialog(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPlateNumber = (EditText) findViewById(R.id.etPlateNumber);
                rgSeats = (RadioGroup) findViewById(R.id.rgSeats);
                rbSeats = (RadioButton) findViewById(rgSeats.getCheckedRadioButtonId());

                progressDialog.setMessage("Adding PUV Please Wait...");
                progressDialog.show();
                plateNumber = etPlateNumber.getText().toString();
                seats = rbSeats.getText().toString();
                addPUV();
            }
        });
    }

    private void addPUV(){
        Firebase ref = new Firebase(Config.FIREBASE_URL);
        PUV puv = new PUV();

        puv.setTerminal(this.terminal);
        puv.setDestination(this.destination);
        puv.setPlateNumber(this.plateNumber);
        puv.setSeat(this.seats);
        puv.setAvailable("not available");

        ref.child("PUV").child(this.terminal).child(this.destination).child(this.plateNumber).setValue(puv);

        Toast.makeText(getBaseContext(), "Add PUV successful.", Toast.LENGTH_LONG).show();

        etPlateNumber.setText("");
        progressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ManagePUVActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("destination",destination);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
