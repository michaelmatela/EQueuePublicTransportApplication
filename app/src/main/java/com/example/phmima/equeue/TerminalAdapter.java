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

/**
 * Created by phmima on 7/24/2017.
 */

public class TerminalAdapter
        extends RecyclerView.Adapter<TerminalAdapter.MyViewHolder> {
    private List<Terminal> terminalList;
    private Context context;
    private ArrayList<StorageReference> storageReferences;

    public void setStorageReferences(ArrayList<StorageReference> storageReferences){
        this.storageReferences = storageReferences;
    }
    public void setContext(Context context){
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTerminal;
        public ImageView ivPic;

        public MyViewHolder(View view){
            super(view);
            tvTerminal = (TextView) view.findViewById(R.id.tvTerminal);
            ivPic = (ImageView) view.findViewById(R.id.ivPic);
        }
    }

    public TerminalAdapter(List<Terminal> terminalList){
        this.terminalList = terminalList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Terminal d = terminalList.get(position);
        StorageReference storageReference = storageReferences.get(position);
        holder.tvTerminal.setText(d.getTerminal().toString());
        Glide.with(this.context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(holder.ivPic);

    }

    @Override
    public int getItemCount(){
        return terminalList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.terminal_items, parent, false);
        return new MyViewHolder(v);
    }

}
