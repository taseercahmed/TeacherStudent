package com.teacherstudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceivedNotifications_Adapter extends RecyclerView.Adapter<ReceivedNotifications_Adapter.ReceivedNotifications_ViewHolder>  {

    Context mCtx;
    ArrayList<ReceivedNotifications_Setter> notificationsList;

    public ReceivedNotifications_Adapter(Context mCtx, ArrayList<ReceivedNotifications_Setter> notificationsList) {
        this.mCtx = mCtx;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public ReceivedNotifications_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_received_notifications_ryclr_design,parent,false);
        return new ReceivedNotifications_Adapter.ReceivedNotifications_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedNotifications_ViewHolder holder, int position) {
        final ReceivedNotifications_Setter receivedNotifications_setter = notificationsList.get(position);
        final String date =  receivedNotifications_setter.getNotificationDate();
        final String time =  receivedNotifications_setter.getNotificationTime();
        final String message =  receivedNotifications_setter.getNotificationMsg();
        final String senderName =  receivedNotifications_setter.getSenderName();
        final String senderEmail =  receivedNotifications_setter.getSenderEmail();

        final String key =  receivedNotifications_setter.getRN_key();
        final String notify =  receivedNotifications_setter.getRN_notify();

        if (notify.contains("true")){
            holder.RN_newBtn.setVisibility(View.VISIBLE);
        }else {
            holder.RN_newBtn.setVisibility(View.GONE);
        }

        holder.notificationDate.setText(date);
        holder.notificationTime.setText(time);
        holder.notificationMsg.setText(message);
//        ReadMoreOption readMoreOption = new ReadMoreOption.Builder(mCtx)
//                .textLength(30,30)
//                .moreLabel("Read More")
//                .lessLabel("Read Less")
//                .moreLabelColor(Color.BLUE)
//                .lessLabelColor(Color.BLUE)
//                .labelUnderLine(false)
//                .build();
//
//        readMoreOption.addReadMoreTo(holder.notificationMsg, message);

        holder.received_notifications_constraint_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReceivedNotifications)mCtx).NotificationDetail(senderName,senderEmail,time+" "+date,message,key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ReceivedNotifications_ViewHolder extends RecyclerView.ViewHolder{
        TextView notificationDate,notificationTime,senderName,senderEmail,notificationMsg,RN_newBtn;
        CardView received_notifications_constraint_layout;

        public ReceivedNotifications_ViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationDate = itemView.findViewById(R.id.notificationDate);
            notificationTime = itemView.findViewById(R.id.notificationTime);
            notificationMsg = itemView.findViewById(R.id.notificationMsg);
            RN_newBtn = itemView.findViewById(R.id.RN_newBtn);
            received_notifications_constraint_layout = itemView.findViewById(R.id.received_notifications_constraint_layout);
        }
    }
}
