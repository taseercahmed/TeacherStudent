package com.teacherstudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityTeacher_Adapter extends RecyclerView.Adapter<ActivityTeacher_Adapter.ActivityTeacher_ViewHolder>  {

    Context mCtx;
    ArrayList<ActivityTeacher_Setter> eventList;

    public ActivityTeacher_Adapter(Context mCtx, ArrayList<ActivityTeacher_Setter> eventList) {
        this.mCtx = mCtx;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ActivityTeacher_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.teacher_rcyclr_design,parent,false);
        return new ActivityTeacher_Adapter.ActivityTeacher_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityTeacher_ViewHolder holder, int position) {
        ActivityTeacher_Setter setter = eventList.get(position);
        String eventDate = setter.getEventDate();
        String eventTitle = setter.getEventTitle();
        String eventDiscription = setter.getEventDiscription();
        String eventStartTime = setter.getEventStartTime();
        String eventEndTime = setter.getEventEndTime();

        holder.eventDate.setText(eventDate);
        holder.eventTitle.setText(eventTitle);
        holder.eventDiscription.setText(eventDiscription);
        holder.eventTime.setText(eventStartTime + " to " + eventEndTime);

    }

    @Override
    public int getItemCount() {
        //return eventList.size();
        int limit = 5;
        if(eventList.size() > limit){
            return limit;
        }
        else{
            return eventList.size();
        }
    }

    public class ActivityTeacher_ViewHolder extends RecyclerView.ViewHolder{
        TextView eventDate,eventTitle,eventDiscription,eventTime;

        public ActivityTeacher_ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventDate = itemView.findViewById(R.id.eventDate);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDiscription = itemView.findViewById(R.id.eventDiscription);
            eventTime = itemView.findViewById(R.id.eventTime);
        }
    }
}
