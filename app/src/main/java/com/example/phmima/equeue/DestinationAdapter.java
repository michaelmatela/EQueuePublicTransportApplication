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

public class DestinationAdapter
       extends RecyclerView.Adapter<DestinationAdapter.MyViewHolder> {
    private List<Destination> destinationList;
    private Context context;
    private ArrayList<StorageReference> storageReferences;

    public void setStorageReferences(ArrayList<StorageReference> storageReferences){
        this.storageReferences = storageReferences;
    }
    public void setContext(Context context){
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDestination;
        public ImageView ivPic;

        public MyViewHolder(View view){
            super(view);
            tvDestination = (TextView) view.findViewById(R.id.tvDestination);
            ivPic = (ImageView) view.findViewById(R.id.ivPic);
        }
    }

    public DestinationAdapter(List<Destination> destinationList){
        this.destinationList = destinationList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Destination d = destinationList.get(position);
        StorageReference storageReference = storageReferences.get(position);
        holder.tvDestination.setText(d.getDestination());
        Glide.with(this.context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(holder.ivPic);

    }

    @Override
    public int getItemCount(){
        return destinationList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destination_items, parent, false);
        return new MyViewHolder(v);
    }

}
