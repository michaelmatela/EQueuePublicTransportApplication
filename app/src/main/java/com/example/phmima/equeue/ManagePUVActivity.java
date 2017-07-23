package com.example.phmima.equeue;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManagePUVActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static String puvItem;
    private static String seatItem;
    private static String availableItem;
    private View view;
    private PUVAdapter pa;
    private ArrayList<PUV> puvList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String destination;

    private String terminal;

    PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_puv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        terminal = preferences.getString("terminal", "");
        Firebase.setAndroidContext(this);
        popup = new PopupMenu(this, view);
        MenuInflater inflater2 = popup.getMenuInflater();
        inflater2.inflate(R.menu.barker_queue_menu, popup.getMenu());


        Bundle extras = getIntent().getExtras();
        destination = extras.getString("destination");
        getPUVList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagePUVActivity.this, AddPUVActivity.class);
                intent.putExtra("destination",destination);
                startActivity(intent);
            }
        });

    }


    private void getPUVList(){
        puvList = new ArrayList<PUV>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("PUV").child(terminal).child(destination);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                RecyclerView rv = (RecyclerView) findViewById(R.id.rvPuv);

                puvList.clear();

                pa = new PUVAdapter(puvList);

                pa.notifyDataSetChanged();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    PUV puv = new PUV();
                    puv.setPlateNumber(ds.child("plateNumber").getValue().toString());
                    puv.setSeat(ds.child("seat").getValue().toString());
                    puv.setAvailable(ds.child("available").getValue().toString());
                    puvList.add(puv);
                }


                pa = new PUVAdapter(puvList);

                pa.notifyDataSetChanged();

                rv.setAdapter(pa);
                LinearLayoutManager llm = new LinearLayoutManager(ManagePUVActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(ManagePUVActivity.this, rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                if (Config.APP_TYPE == 1){
                                    PopupMenu popup = new PopupMenu(ManagePUVActivity.this, v);
                                    MenuInflater inflater = popup.getMenuInflater();
                                    inflater.inflate(R.menu.barker_puv_menu, popup.getMenu());
                                    popup.show();

                                    popup.setOnMenuItemClickListener(ManagePUVActivity.this);
                                    puvItem = ((TextView) v.findViewById(R.id.tvPlateNumber)).getText().toString();
                                    seatItem = ((TextView) v.findViewById(R.id.tvSeat)).getText().toString();
                                    availableItem = ((TextView) v.findViewById(R.id.tvAvailable)).getText().toString();

                                }
                                else if (Config.APP_TYPE == 2){

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
            case R.id.mnuProcessPUV:
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagePUVActivity.this);
                builder.setTitle("Process PUV");
                builder.setMessage("Are you sure you want to process PUV?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Firebase ref = new Firebase(Config.FIREBASE_URL);
                        PUV puv = new PUV();
                        puv.setTerminal(terminal);
                        puv.setDestination(destination);
                        puv.setPlateNumber(puvItem);
                        puv.setSeat(seatItem);
                        if (availableItem.equals("not available"))
                            availableItem = "available";
                        else
                            availableItem = "not available";

                        puv.setAvailable(availableItem);

                        ref.child("PUV").child(terminal).child(destination).child(puvItem).setValue(puv);

                        Toast.makeText(getBaseContext(), "Process puv successful.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
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
