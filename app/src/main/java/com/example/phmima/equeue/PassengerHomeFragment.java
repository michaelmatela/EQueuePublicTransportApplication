package com.example.phmima.equeue;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PassengerHomeFragment extends Fragment {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvEmail;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_passenger_home, container, false);

        tvFirstName = (TextView) view.findViewById(R.id.tvFirstName);
        tvLastName = (TextView) view.findViewById(R.id.tvLastName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        getAccountData();

        return view;
    }

    private void getAccountData(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Profile").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tvFirstName.setText(snapshot.child("firstName").getValue().toString());
                tvLastName.setText(snapshot.child("lastName").getValue().toString());
                tvEmail.setText(snapshot.child("email").getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
