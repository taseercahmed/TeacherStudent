package com.teacherstudent.Student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.teacherstudent.R;

import java.util.ArrayList;


public class StudentSubjects_Adapter extends RecyclerView.Adapter<StudentSubjects_Adapter.StudentSubjects_ViewHolder>  {

    Context mCtx;
    ArrayList<StudentSubjects_Setter> subjectNames;

    public StudentSubjects_Adapter(Context mCtx, ArrayList<StudentSubjects_Setter> subjectNames) {
        this.mCtx = mCtx;
        this.subjectNames = subjectNames;
    }

    @NonNull
    @Override
    public StudentSubjects_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_student_subjects_rcyclrdsign,parent,false);
        return new StudentSubjects_Adapter.StudentSubjects_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentSubjects_ViewHolder holder, int position) {
        StudentSubjects_Setter studentSubjects_setter = subjectNames.get(position);
        final String subject = studentSubjects_setter.getStudentSubject_name();

        holder.student_subject_name.setText(subject);
        holder.subjectNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StudentSubjects)mCtx).openRelatedActivity(subject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectNames.size();
    }

    public class StudentSubjects_ViewHolder extends RecyclerView.ViewHolder{
        TextView student_subject_name;
        ConstraintLayout subjectNameLayout;

        public StudentSubjects_ViewHolder(@NonNull View itemView) {
            super(itemView);

            student_subject_name = itemView.findViewById(R.id.student_subject_name);
            subjectNameLayout =itemView.findViewById(R.id.subjectNameLayout);
        }
    }
}
