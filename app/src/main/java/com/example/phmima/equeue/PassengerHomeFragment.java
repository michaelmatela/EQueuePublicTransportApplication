package com.example.phmima.equeue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.firebase.client.Firebase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class PassengerHomeFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private EditText etFirstName;
    private EditText etLastName;
    private TextView tvEmail;
    private ImageView ivPic;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Button btnCancel;
    private Button btnEdit;
    private Button btnRemoveQueue;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Firebase.setAndroidContext(getContext());
        view = inflater.inflate(R.layout.fragment_passenger_home, container, false);

        etFirstName = (EditText) view.findViewById(R.id.etFirstName);
        etLastName = (EditText) view.findViewById(R.id.etLastName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        ivPic = (ImageView) view.findViewById(R.id.ivPic);

        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnRemoveQueue = (Button) view.findViewById(R.id.btnRemoveQueue);

        btnEdit.setText("Edit");
        btnCancel.setEnabled(false);

        SharedPreferences preferences = getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE);

        String terminal = preferences.getString("terminal", "");
        String destination = preferences.getString("destination", "");
        String queue = preferences.getString("queue", "");
        String queue_date = preferences.getString("time", "");

        if (!terminal.isEmpty() || !destination.isEmpty() || !queue.isEmpty() || !queue_date.isEmpty()){
            View  view_passenger_queue = view.findViewById(R.id.passenger_queue_details);
            view_passenger_queue.setVisibility(View.VISIBLE);
            TextView tvTerminal = (TextView)view.findViewById(R.id.tvTerminal);
            TextView tvDestination = (TextView) view.findViewById(R.id.tvDestination);
            TextView tvQueue = (TextView) view.findViewById(R.id.tvQueue);
            TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvTerminal.setText(terminal);
            tvDestination.setText(destination);
            tvQueue.setText(queue);
            tvDate.setText(queue_date);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnEdit.getText().toString().equals("Edit")) {
                    btnEdit.setText("Save");
                    etFirstName.setEnabled(true);
                    etLastName.setEnabled(true);
                    etFirstName.setFocusable(true);
                    etLastName.setFocusable(true);
                    btnCancel.setEnabled(true);
                    btnCancel.setVisibility(view.VISIBLE);
                }
                else if(btnEdit.getText().toString().equals("Save")){
                    etFirstName.setEnabled(false);
                    etLastName.setEnabled(false);
                    etFirstName.setFocusable(false);
                    etLastName.setFocusable(false);
                    btnEdit.setText("Edit");
                    btnCancel.setEnabled(false);
                    btnCancel.setVisibility(view.INVISIBLE);
                    setProfile();
                }
            }
        });

        btnRemoveQueue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("terminal", "");
                editor.putString("destination", "");
                editor.putString("queue", "");
                editor.putString("time", "");
                editor.commit();
                View  view_passenger_queue = view.findViewById(R.id.passenger_queue_details);
                view_passenger_queue.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "You are now removed from queue", Toast.LENGTH_LONG).show();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                    etFirstName.setEnabled(false);
                    etLastName.setEnabled(false);
                    etFirstName.setFocusable(false);
                    etLastName.setFocusable(false);
                    btnEdit.setText("Edit");
                    btnCancel.setEnabled(false);
                    btnCancel.setVisibility(view.INVISIBLE);
            }
        });

        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityFromFragment(PassengerHomeFragment.this,pickPhoto , 1);//one can be replaced with any action code
            }
        });
        getAccountData();
        return view;
    }

    private void setProfile(){
        if (ivPic.getTag() != null){
            user = mAuth.getCurrentUser();
            Firebase ref = new Firebase(Config.FIREBASE_URL);
            Profile profile = new Profile();

            profile.setFirstName(etFirstName.getText().toString());
            profile.setLastName(etLastName.getText().toString());
            profile.setEmail(tvEmail.getText().toString());

            ref.child("Profile").child(user.getUid());

            String path = ivPic.getTag().toString();
            Uri uri = Uri.parse(path);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference().child("Profile").child(user.getUid());
            storageRef.putFile(uri);

            Toast.makeText(getActivity(), "Profile edit successful.", Toast.LENGTH_LONG).show();

        }
        else{
            user = mAuth.getCurrentUser();
            Firebase ref = new Firebase(Config.FIREBASE_URL);
            Profile profile = new Profile();

            profile.setFirstName(etFirstName.getText().toString());
            profile.setLastName(etLastName.getText().toString());
            profile.setEmail(tvEmail.getText().toString());

            ref.child("Profile").child(user.getUid());

            Toast.makeText(getActivity(), "Profile edit successful.", Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
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

    private void getAccountData(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                etFirstName.setText(snapshot.child("firstName").getValue().toString());
                etLastName.setText(snapshot.child("lastName").getValue().toString());
                tvEmail.setText(snapshot.child("email").getValue().toString());

                mAuth = FirebaseAuth.getInstance();

                user = mAuth.getCurrentUser();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReference().child("Profile").child(user.getUid());


                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext())
                                .using(new FirebaseImageLoader())
                                .load(storageRef)
                                .into(ivPic);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
