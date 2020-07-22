package com.teacherstudent.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teacherstudent.R;

import java.util.ArrayList;

public class S_Notifications_TeacherAdapter extends RecyclerView.Adapter<S_Notifications_TeacherAdapter.S_Notifications_TeacherVewHolder> {

    Context mCtx;
    ArrayList<S_Notifications_AdminAdapter_Setter> teacherNotificationList;

    public S_Notifications_TeacherAdapter(Context mCtx, ArrayList<S_Notifications_AdminAdapter_Setter> teacherNotificationList) {
        this.mCtx = mCtx;
        this.teacherNotificationList = teacherNotificationList;
    }

    @NonNull
    @Override
    public S_Notifications_TeacherVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_sent_notifications_ryclr_design,parent,false);
        return new S_Notifications_TeacherAdapter.S_Notifications_TeacherVewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull S_Notifications_TeacherVewHolder holder, int position) {
        S_Notifications_AdminAdapter_Setter setter = teacherNotificationList.get(position);
        final String date = setter.getSN_date();
        final String message = setter.getSN_message();
        final String senderEmail = setter.getSN_senderEmail();
        final String senderName = setter.getSN_senderName();
        final String time = setter.getSN_time();
        final String notify = setter.getSN_notify();
        final String key = setter.getSN_key();

        if (notify.contains("true")){
            holder.SN_newBtn.setVisibility(View.VISIBLE);
        }else {
            holder.SN_newBtn.setVisibility(View.GONE);
        }

        holder.SN_sentDate.setText(setter.getSN_date());
        holder.SN_sentMessage.setText(setter.getSN_message());
        holder.SN_sentTime.setText(setter.getSN_time());

        holder.SN_detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((S_Notifications)mCtx).NotificationDetail(senderName,senderEmail,time+" "+date,message,"Teacher",key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherNotificationList.size();
    }

    public class S_Notifications_TeacherVewHolder extends RecyclerView.ViewHolder{
        TextView SN_sentDate,SN_sentMessage,SN_sentTime;
        TextView SN_detailBtn,SN_newBtn;

        public S_Notifications_TeacherVewHolder(@NonNull View itemView) {
            super(itemView);

            SN_sentDate = itemView.findViewById(R.id.SN_sentDate);
            SN_sentMessage = itemView.findViewById(R.id.SN_sentMessage);
            SN_sentTime = itemView.findViewById(R.id.SN_sentTime);

            SN_detailBtn = itemView.findViewById(R.id.SN_detailBtn);
            SN_newBtn = itemView.findViewById(R.id.SN_newBtn);
        }
    }

}
