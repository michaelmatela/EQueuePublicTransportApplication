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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.phmima.equeue.TabFragment.tabLayout;

public class ManageQueueActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    private static int currentCount = 0;
    private static String queueItem;
    private View view;
    private QueueAdapter qa;
    private ArrayList<Queue> queueList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String destination;

    private String terminal;

    PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_queue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        terminal = preferences.getString("terminal", "");

        popup = new PopupMenu(this, view);
        MenuInflater inflater2 = popup.getMenuInflater();
        inflater2.inflate(R.menu.barker_queue_menu, popup.getMenu());


        Bundle extras = getIntent().getExtras();
        destination = extras.getString("destination");
        getQueueList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageQueueActivity.this);
                builder.setTitle("Add Queue");
                builder.setMessage("Are you sure you want to add queue?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Firebase ref = new Firebase(Config.FIREBASE_URL);
                        Queue queue = new Queue();
                        queue.setTerminal(terminal);
                        queue.setDestination(destination);
                        queue.setStatus("Queueing");
                        queue.setTime(String.valueOf(new Date().getTime()));
                        queue.setQueue(String.valueOf(currentCount+1));

                        ref.child("Queue").child(terminal).child(destination).child(String.valueOf(currentCount+1)).setValue(queue);

                        Toast.makeText(getBaseContext(), "Add queue successful.", Toast.LENGTH_LONG).show();

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
            }
        });

        if (Config.APP_TYPE == 2)
            fab.hide();
    }

    private void getQueueList(){
        queueList = new ArrayList<Queue>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Queue").child(terminal).child(destination);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                RecyclerView rv = (RecyclerView) findViewById(R.id.rvQueue);

                queueList.clear();

                qa = new QueueAdapter(queueList);

                qa.notifyDataSetChanged();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String epochString = ds.child("time").getValue().toString();
                    long epoch = Long.parseLong( epochString );
                    Date date = new Date( epoch  );
                    Queue queue = new Queue();
                    queue.setQueue(ds.child("queue").getValue().toString());
                    queue.setStatus(ds.child("status").getValue().toString());
                    queue.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
                    queueList.add(queue);
                }
                currentCount = queueList.size();
                ArrayList<Queue> newQueueList = new ArrayList<Queue>();

                for (Queue q : queueList) {
                    Queue q1 = new Queue();
                    q1.setQueue(q.getQueue());
                    q1.setStatus(q.getStatus());
                    q1.setTime(q.getTime());

                    if (q.getStatus().equals("Queueing"))
                        newQueueList.add(q1);
                }

                qa = new QueueAdapter(newQueueList);

                qa.notifyDataSetChanged();







                qa.notifyDataSetChanged();
                rv.setAdapter(qa);
                LinearLayoutManager llm = new LinearLayoutManager(ManageQueueActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(ManageQueueActivity.this, rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                if (Config.APP_TYPE == 1){
                                    PopupMenu popup = new PopupMenu(ManageQueueActivity.this, v);
                                    MenuInflater inflater = popup.getMenuInflater();
                                    inflater.inflate(R.menu.barker_queue_menu, popup.getMenu());
                                    popup.show();

                                    popup.setOnMenuItemClickListener(ManageQueueActivity.this);
                                    queueItem = ((TextView) v.findViewById(R.id.tvQueue)).getText().toString();

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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuProcessQueue:
                AlertDialog.Builder builder = new AlertDialog.Builder(ManageQueueActivity.this);
                builder.setTitle("Process Queue");
                builder.setMessage("Are you sure you want to process queue?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        Firebase ref = new Firebase(Config.FIREBASE_URL);
                        Queue queue = new Queue();
                        queue.setTerminal(terminal);
                        queue.setDestination(destination);
                        queue.setStatus("Processed");
                        queue.setTime(String.valueOf(new Date().getTime()));
                        queue.setQueue(queueItem);

                        ref.child("Queue").child(terminal).child(destination).child(queueItem).setValue(queue);

                        Toast.makeText(getBaseContext(), "Process queue successful.", Toast.LENGTH_LONG).show();
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
}
