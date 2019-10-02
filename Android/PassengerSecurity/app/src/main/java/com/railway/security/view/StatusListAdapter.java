package com.railway.security.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.railway.security.R;
import com.railway.security.model.FIR;
import com.railway.security.model.User;

import java.util.List;

/**
 * Created by Admin on 03-Dec-17.
 */

public class StatusListAdapter extends RecyclerView.Adapter<StatusViewHolder>{

    List<FIR> list;
    Context context;

    public StatusListAdapter(List<FIR> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public StatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);
        StatusViewHolder viewHolder = new StatusViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StatusViewHolder holder, final int position) {
        final FIR fir = list.get(position);
        holder.time.setText(fir.time);
        holder.firNo.setText(fir.firNo);
        holder.status.setText(fir.status);

        switch(fir.status){
            case "APPROVED" : holder.status.setTextColor(context.getResources().getColor(R.color.colorApproved)); break;
            case "REJECTED" : holder.status.setTextColor(context.getResources().getColor(R.color.colorRejected)); break;
            case "PENDING" : holder.status.setTextColor(context.getResources().getColor(R.color.colorPending)); break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<FIR> list){
        this.list = list;
        notifyDataSetChanged();
    }

}
