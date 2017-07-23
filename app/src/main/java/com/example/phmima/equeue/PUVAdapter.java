package com.example.phmima.equeue;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class PUVAdapter
        extends RecyclerView.Adapter<PUVAdapter.MyViewHolder> {
    private List<PUV> puvList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvPlateNumber;
        public TextView tvSeat;
        public TextView tvAvailable;

        public MyViewHolder(View view){
            super(view);
            tvPlateNumber = (TextView) view.findViewById(R.id.tvPlateNumber);
            tvSeat = (TextView) view.findViewById(R.id.tvSeat);
            tvAvailable = (TextView) view.findViewById(R.id.tvAvailable);
        }
    }

    public PUVAdapter(List<PUV> puvList){
        this.puvList = puvList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        PUV d = puvList.get(position);
        holder.tvPlateNumber.setText(d.getPlateNumber());
        holder.tvSeat.setText(d.getSeat());
        holder.tvAvailable.setText(d.getAvailable());
    }

    @Override
    public int getItemCount(){
        return puvList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.puv_item, parent, false);
        return new MyViewHolder(v);
    }

}
