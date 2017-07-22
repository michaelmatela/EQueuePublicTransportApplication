package com.example.phmima.equeue;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DestinationAdapter
       extends RecyclerView.Adapter<DestinationAdapter.MyViewHolder> {
    private List<Destination> destinationList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDestination;
        public TextView tvQueueCount;
        public TextView tvCurrentCount;

        public MyViewHolder(View view){
            super(view);
            tvDestination = (TextView) view.findViewById(R.id.tvDestination);
            tvQueueCount = (TextView) view.findViewById(R.id.tvQueueCount);
            tvCurrentCount = (TextView) view.findViewById(R.id.tvCurrentCount);
        }
    }

    public DestinationAdapter(List<Destination> destinationList){
        this.destinationList = destinationList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Destination d = destinationList.get(position);
        holder.tvDestination.setText(d.getDestination());
        holder.tvQueueCount.setText(d.getQueueCount());
        holder.tvCurrentCount.setText(d.getCurrentCount());
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
