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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddDestinationActivity extends AppCompatActivity {

    EditText etDestination;
    String destination;
    String terminal;
    Button btnAdd;
    private ProgressDialog progressDialog;

    ImageView ivPic;
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



        ivPic = (ImageView) findViewById(R.id.ivPic);

        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    ivPic.setImageURI(selectedImage);
                    ivPic.setTag(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    ivPic.setImageURI(selectedImage);
                    ivPic.setTag(selectedImage);
                }
                break;
        }
    }

    private void addDestination(){
        if (ivPic.getTag() != null){
            Firebase ref = new Firebase(Config.FIREBASE_URL);
            Destination destination = new Destination();

            destination.setTerminal(this.terminal);
            destination.setDestination(this.destination);
            destination.setPhoto("");

            ref.child("Destination").child(this.terminal).child(this.destination).setValue(destination);

            String path = ivPic.getTag().toString();
            Uri uri = Uri.parse(path);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("Destination").child(this.terminal).child(this.destination);
            storageRef.putFile(uri);



            Toast.makeText(getBaseContext(), "Add destination successful.", Toast.LENGTH_LONG).show();

            etDestination.setText("");
        }
        else{
            Toast.makeText(getBaseContext(), "Please choose a photo for this destination", Toast.LENGTH_LONG).show();
        }
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
