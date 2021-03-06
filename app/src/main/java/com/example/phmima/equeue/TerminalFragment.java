package com.example.phmima.equeue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class TerminalFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{

    private static String terminalItem;
    private View view;
    private TerminalAdapter da;
    private ArrayList<Terminal> terminalList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    PopupMenu popup;
    private String terminal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_barker_destination, container, false);

        popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater2 = popup.getMenuInflater();
        inflater2.inflate(R.menu.terminal_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(this);

        getTerminalList();

        return view;
    }

    private void getTerminalList(){
        terminalList = new ArrayList<Terminal>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("TerminalList");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                ArrayList<StorageReference> storageReferences = new ArrayList<StorageReference>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference().child("Terminal").child(ds.getValue().toString()).child("photo.jpg");
                    Terminal terminal = new Terminal();
                    terminal.setTerminal(ds.getValue().toString());
                    storageReferences.add(storageRef);

                    terminalList.add(terminal);
                }

                da = new TerminalAdapter(terminalList);
                da.setContext(getContext());


                da.setStorageReferences(storageReferences);

                RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvDestination);

                rv.setAdapter(da);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(getContext(), rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                System.out.println(Config.APP_TYPE);
                                if (Config.APP_TYPE == 1){
                                    popup.show();
                                    terminalItem = ((TextView)v.findViewById(R.id.tvDestination)).getText().toString();
                                }
                                else if (Config.APP_TYPE == 2){
                                    popup.show();
                                    terminalItem = ((TextView)v.findViewById(R.id.tvTerminal)).getText().toString();
                                }
                            }

                            public void onLongClickItem(View v, int position) {

                            }
                        }));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }




    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuDestination:
                Intent intentDestination = new Intent(getActivity(), DestinationActivity.class);
                intentDestination.putExtra("terminal", terminalItem);
                getActivity().startActivity(intentDestination);
                return true;

            default:
                return false;
        }
    }



}
