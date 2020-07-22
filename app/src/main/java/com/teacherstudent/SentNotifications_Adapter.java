package com.teacherstudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SentNotifications_Adapter extends RecyclerView.Adapter<SentNotifications_Adapter.SentNotifications_ViewHolder>   {

    Context mCtx;
    ArrayList<SentNotifications_Setter> sentNotificationsList;

    public SentNotifications_Adapter(Context mCtx, ArrayList<SentNotifications_Setter> sentNotificationsList) {
        this.mCtx = mCtx;
        this.sentNotificationsList = sentNotificationsList;
    }

    @NonNull
    @Override
    public SentNotifications_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_sent_notifications_ryclr_design,parent,false);
        return new SentNotifications_Adapter.SentNotifications_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SentNotifications_ViewHolder holder, int position) {
        final SentNotifications_Setter sentNotifications_setter = sentNotificationsList.get(position);
        final String SN_date =  sentNotifications_setter.getSN_date();
        final String SN_message =  sentNotifications_setter.getSN_messae();
        final String SN_receiverEmails =  sentNotifications_setter.getSN_receiverEmails();
        final String SN_sendedTo =  sentNotifications_setter.getSN_sendedTo();
        final String SN_senderEmail =  sentNotifications_setter.getSN_senderEmail();
        final String SN_senderName =  sentNotifications_setter.getSN_senderName();
        final String SN_subject =  sentNotifications_setter.getSN_subject();
        final String SN_time =  sentNotifications_setter.getSN_time();

        holder.sentMessage.setText(SN_message);
        holder.sentDate.setText(SN_date);
        holder.sentTime.setText(SN_time);
        holder.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SentNotifications)mCtx).SN_detailDialogBox(SN_sendedTo,SN_receiverEmails,SN_date,SN_time,SN_subject,SN_message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sentNotificationsList.size();
    }

    public class SentNotifications_ViewHolder extends RecyclerView.ViewHolder{
        TextView sentMessage,detailBtn,sentDate,sentTime;

        public SentNotifications_ViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessage = itemView.findViewById(R.id.SN_sentMessage);
            detailBtn = itemView.findViewById(R.id.SN_detailBtn);
            sentDate = itemView.findViewById(R.id.SN_sentDate);
            sentTime = itemView.findViewById(R.id.SN_sentTime);
        }
    }
}
