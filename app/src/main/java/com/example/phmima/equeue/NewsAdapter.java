package com.example.phmima.equeue;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter
       extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private List<News> newsList;
    private Context context;
    private ArrayList<StorageReference> storageReferences;

    public void setStorageReferences(ArrayList<StorageReference> storageReferences){
        this.storageReferences = storageReferences;
    }
    public void setContext(Context context){
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvBody;
        public TextView tvTimeStamp;
        public TextView tvId;

        public MyViewHolder(View view){
            super(view);
            tvBody = (TextView) view.findViewById(R.id.tvBody);
            tvTimeStamp = (TextView) view.findViewById(R.id.tvTimeStamp);
            tvId = (TextView) view.findViewById(R.id.tvId);
        }
    }

    public NewsAdapter(List<News> newsList){
        this.newsList = newsList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        News d = newsList.get(position);
        holder.tvBody.setText(d.getBody());
        holder.tvTimeStamp.setText(d.getTimestamp());
        holder.tvId.setText(d.getId());


    }

    @Override
    public int getItemCount(){
        return newsList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        return new MyViewHolder(v);
    }

}
