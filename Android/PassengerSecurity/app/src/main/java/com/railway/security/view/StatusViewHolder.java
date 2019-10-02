package com.railway.security.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.railway.security.R;

public class StatusViewHolder extends RecyclerView.ViewHolder{
    TextView firNo;
    TextView status;
    TextView time;


    public StatusViewHolder(View itemView) {
        super(itemView);
        firNo = (TextView) itemView.findViewById(R.id.status_firno);
        time = (TextView) itemView.findViewById(R.id.status_time);
        status = (TextView) itemView.findViewById(R.id.status_status);
    }
}