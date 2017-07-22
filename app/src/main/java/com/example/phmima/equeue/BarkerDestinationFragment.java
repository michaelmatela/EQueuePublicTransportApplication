package com.example.phmima.equeue;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.content.Context.MODE_PRIVATE;


public class BarkerDestinationFragment extends Fragment {

    private View view;
    private DestinationAdapter da;
    private ArrayList<Destination> destinationList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String terminal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_barker_destination, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE);
        terminal = preferences.getString("terminal", "");

        getDestinationList();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.show();
        return view;
    }

    private void getDestinationList(){
        destinationList = new ArrayList<Destination>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Destination").child(terminal);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Destination destination = new Destination();
                    destination.setDestination(ds.child("destination").getValue().toString());
                    destination.setQueueCount(ds.child("queueCount").getValue().toString());
                    destination.setCurrentCount(ds.child("currentCount").getValue().toString());
                    destination.setPhoto("");
                    destination.setTerminal(ds.child("terminal").getValue().toString());

                    destinationList.add(destination);
                }

                da = new DestinationAdapter(destinationList);

                RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvDestination);

                rv.setAdapter(da);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(getContext(), rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {

                            }

                            public void onLongClickItem(View v, int position) {
                                System.out.println("On Long Click Item interface");
                            }
                        }));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
