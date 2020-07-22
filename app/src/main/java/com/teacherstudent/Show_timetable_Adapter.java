package com.teacherstudent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Show_timetable_Adapter extends RecyclerView.Adapter<Show_timetable_Adapter.WriteNotification_ViewHolder> {

    public List<ClassSectionGroup> classes = new ArrayList();
    Context mCtx;


    public Show_timetable_Adapter(Context mCtx, List<ClassSectionGroup> classes) {
        this.mCtx = mCtx;
        this.classes = classes;
    }

    @NonNull
    @Override
    public WriteNotification_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.timetablerecyclerdesign, parent, false);
        return new WriteNotification_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WriteNotification_ViewHolder holder, int position) {
        final ClassSectionGroup model = classes.get(position);

        final String className = model.getClassName();
        final String sectionName = model.getSectionName();
        final String studyGroup = model.getStudyGroup();

        holder.classname.setText(className);
        holder.sectionName.setText(sectionName);
        holder.studyGroup.setText(studyGroup);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, TimetableActivityTeacher.class);
                intent.putExtra("className", className);
                intent.putExtra("sectionName", sectionName);
                intent.putExtra("studyGroup", studyGroup);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }


    public class WriteNotification_ViewHolder extends RecyclerView.ViewHolder {

        TextView classname, sectionName, studyGroup;
        CardView cardView;

        public WriteNotification_ViewHolder(@NonNull View itemView) {
            super(itemView);
            classname = itemView.findViewById(R.id.TF_className);
            sectionName = itemView.findViewById(R.id.TF_sectionName);
            studyGroup = itemView.findViewById(R.id.TF_studyGroup);

            cardView = itemView.findViewById(R.id.TF_cardView);
        }
    }
}
