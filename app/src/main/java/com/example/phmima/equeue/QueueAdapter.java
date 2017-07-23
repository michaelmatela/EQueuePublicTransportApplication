package com.example.phmima.equeue;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.List;

public class QueueAdapter
       extends RecyclerView.Adapter<QueueAdapter.MyViewHolder> {
    private List<Queue> queueList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvQueue;
        public TextView tvTime;
        public TextView tvStatus;

        public MyViewHolder(View view){
            super(view);
            tvQueue = (TextView) view.findViewById(R.id.tvQueue);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        }
    }

    public QueueAdapter(List<Queue> queueList){
        this.queueList = queueList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Queue d = queueList.get(position);
        holder.tvQueue.setText(d.getQueue());
        holder.tvTime.setText(d.getTime());
        holder.tvStatus.setText(d.getStatus());


    }

    @Override
    public int getItemCount(){
        return queueList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.queue_items, parent, false);
        return new MyViewHolder(v);
    }

}
