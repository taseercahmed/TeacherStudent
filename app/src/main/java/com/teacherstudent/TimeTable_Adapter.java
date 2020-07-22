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

public class TimeTable_Adapter extends RecyclerView.Adapter<TimeTable_Adapter.TimeTable_ViewHolder>   {

    Context mCtx;
    ArrayList<TimeTable_Setter> subjectsList;

    public TimeTable_Adapter(Context mCtx, ArrayList<TimeTable_Setter> subjectsList) {
        this.mCtx = mCtx;
        this.subjectsList = subjectsList;
    }

    @NonNull
    @Override
    public TimeTable_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_time_table_ryclr_design,parent,false);
        return new TimeTable_Adapter.TimeTable_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTable_ViewHolder holder, int position) {
        final TimeTable_Setter timeTable_setter = subjectsList.get(position);

        final String classNo = timeTable_setter.getClassNumber();
        final String sectionName = timeTable_setter.getSection();
        final String studyGroup = timeTable_setter.getStudyGroup();
        final String subjectName = timeTable_setter.getSubjectName();

        final String monday = timeTable_setter.getMonday();
        final String tuesday = timeTable_setter.getTuesday();
        final String wednesday = timeTable_setter.getWednesday();
        final String thursday = timeTable_setter.getThursday();
        final String friday = timeTable_setter.getFriday();
        final String saturday = timeTable_setter.getSaturday();


        holder.classNo.setText(classNo);
        holder.sectionName.setText(sectionName);
        holder.studyGroup.setText(studyGroup);
        holder.subjectName.setText(subjectName);

        holder.monday.setText(monday);
        holder.tuesday.setText(tuesday);
        holder.wednesday.setText(wednesday);
        holder.thursday.setText(thursday);
        holder.friday.setText(friday);
        holder.saturday.setText(saturday);

        /*holder.timeTableCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx,SelectionPage.class);
                i.putExtra("classNo",classNo);
                i.putExtra("sectionName",sectionName);
                i.putExtra("studyGroup",studyGroup);
                i.putExtra("subjectName",subjectName);
                mCtx.startActivity(i);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public class TimeTable_ViewHolder extends RecyclerView.ViewHolder{
        TextView classNo,sectionName,studyGroup,subjectName;
        TextView monday,tuesday,wednesday,thursday,friday,saturday;
        CardView timeTableCardView;

        public TimeTable_ViewHolder(@NonNull View itemView) {
            super(itemView);

            classNo = itemView.findViewById(R.id.classNo);
            sectionName = itemView.findViewById(R.id.sectionName);
            studyGroup = itemView.findViewById(R.id.studyGroup);
            subjectName = itemView.findViewById(R.id.subjectName);
            timeTableCardView = itemView.findViewById(R.id.timeTableCardView);

            monday = itemView.findViewById(R.id.monday);
            tuesday = itemView.findViewById(R.id.tuesday);
            wednesday = itemView.findViewById(R.id.wednesday);
            thursday = itemView.findViewById(R.id.thursday);
            friday = itemView.findViewById(R.id.friday);
            saturday = itemView.findViewById(R.id.saturday);
        }
    }
}
