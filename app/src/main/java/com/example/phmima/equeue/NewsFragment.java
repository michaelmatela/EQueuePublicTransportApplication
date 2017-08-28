package com.example.phmima.equeue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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


public class NewsFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{
    private static String newsItem;
    private View view;
    private NewsAdapter da;
    private ArrayList<News> newsList;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    PopupMenu popup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);

        popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater2 = popup.getMenuInflater();
        inflater2.inflate(R.menu.barker_destination_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(this);

        getNewsList();

        return view;
    }

    private void getNewsList(){
        newsList = new ArrayList<News>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("News");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    News news = new News();

                    try {
                        news.setBody(ds.child("body").getValue().toString());
                        news.setTimestamp(ds.child("timestamp").getValue().toString());
                        news.setId(ds.child("id").getValue().toString());
                        newsList.add(news);
                    }catch(NullPointerException a){

                    }
                }

                da = new NewsAdapter(newsList);
                da.setContext(getContext());
                da.notifyDataSetChanged();
                RecyclerView rv = (RecyclerView) view.findViewById(R.id.rvNews);

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
                                    newsItem = ((TextView)v.findViewById(R.id.tvDestination)).getText().toString();
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
            case R.id.mnuEditDestination:
                Intent intentQueue = new Intent(getActivity(), ManageQueueActivity.class);
                intentQueue.putExtra("destination", newsItem);
                getActivity().startActivity(intentQueue);
                return true;
            case R.id.mnuManagePUV:
                Intent intentPUV = new Intent(getActivity(), ManagePUVActivity.class);
                intentPUV.putExtra("destination", newsItem);
                getActivity().startActivity(intentPUV);
                return true;
            default:
                return false;
        }
    }


}
