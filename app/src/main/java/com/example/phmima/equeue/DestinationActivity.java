package com.example.phmima.equeue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DestinationActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    private static String destinationItem;
    private View view;
    private DestinationAdapter da;
    private ArrayList<Destination> destinationList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    PopupMenu popup;
    private String terminal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        popup = new PopupMenu(this, view);
        MenuInflater inflater2 = popup.getMenuInflater();
        inflater2.inflate(R.menu.passenger_destination_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(this);

        Bundle extras = getIntent().getExtras();
        terminal = extras.getString("terminal");
        setTitle(terminal);

        getDestinationList();
    }


    private void getDestinationList(){
        destinationList = new ArrayList<Destination>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Destination").child(terminal);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                ArrayList<StorageReference> storageReferences = new ArrayList<StorageReference>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference().child("Destination").child(ds.child("terminal").getValue().toString()).child(ds.child("destination").getValue().toString());
                    Destination destination = new Destination();
                    try {
                        destination.setDestination(ds.child("destination").getValue().toString());
                        destination.setPhoto("");
                        destination.setTerminal(ds.child("terminal").getValue().toString());
                        storageReferences.add(storageRef);

                        destinationList.add(destination);
                    }
                    catch(NullPointerException a){}
                }

                da = new DestinationAdapter(destinationList);
                da.setContext(DestinationActivity.this);


                da.setStorageReferences(storageReferences);

                RecyclerView rv = (RecyclerView) findViewById(R.id.rvDestination);

                rv.setAdapter(da);
                LinearLayoutManager llm = new LinearLayoutManager(DestinationActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(DestinationActivity.this, rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                if (Config.APP_TYPE == 1){
                                    popup.show();
                                    destinationItem = ((TextView)v.findViewById(R.id.tvDestination)).getText().toString();
                                }
                                else if (Config.APP_TYPE == 2){
                                    PopupMenu popup = new PopupMenu(DestinationActivity.this, v);
                                    MenuInflater inflater = popup.getMenuInflater();
                                    inflater.inflate(R.menu.passenger_destination_menu, popup.getMenu());
                                    popup.setOnMenuItemClickListener(DestinationActivity.this);
                                    popup.show();
                                    destinationItem = ((TextView)v.findViewById(R.id.tvDestination)).getText().toString();
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
            case R.id.mnuQueue:
                Intent intentQueue = new Intent(DestinationActivity.this, ManageQueueActivity.class);
                intentQueue.putExtra("destination", destinationItem);
                intentQueue.putExtra("terminal", terminal);
                DestinationActivity.this.startActivity(intentQueue);
                return true;
            case R.id.mnuPUV:
                Intent intentPUV = new Intent(DestinationActivity.this, ManagePUVActivity.class);
                intentPUV.putExtra("destination", destinationItem);
                intentPUV.putExtra("terminal", terminal);
                DestinationActivity.this.startActivity(intentPUV);
                return true;
            case R.id.mnuViewMap:
                Intent intentMap = new Intent(DestinationActivity.this, MapsActivity.class);
                intentMap.putExtra("destination", destinationItem);
                intentMap.putExtra("terminal", terminal);
                DestinationActivity.this.startActivity(intentMap);
                return true;
            default:
                return false;
        }
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
