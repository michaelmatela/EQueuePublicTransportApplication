package com.example.phmima.equeue;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNews extends AppCompatActivity {

    EditText etBody;
    String body;
    Button btnAdd;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(this);

        etBody = (EditText) findViewById(R.id.etBody);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        progressDialog = new ProgressDialog(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Registering Please Wait...");
                progressDialog.show();
                body = etBody.getText().toString();
                addNews();
            }
        });
    }

    private void addNews(){
        String dateEpoch = String.valueOf(new Date().getTime());
        long epoch = Long.parseLong( dateEpoch );
        Date date = new Date( epoch  );
        String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        Firebase ref = new Firebase(Config.FIREBASE_URL);
        News news = new News();

        news.setBody(this.body);

        ref.child("News").child(dateEpoch).setValue(dateEpoch);
        ref.child("News").child(dateEpoch).child("body").setValue(this.body);
        ref.child("News").child(dateEpoch).child("id").setValue(dateEpoch);
        ref.child("News").child(dateEpoch).child("timestamp").setValue(dateString);

        Toast.makeText(getBaseContext(), "Add destination successful.", Toast.LENGTH_LONG).show();

        etBody.setText("");

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
