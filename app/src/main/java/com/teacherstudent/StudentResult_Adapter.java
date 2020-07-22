package com.teacherstudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class StudentResult_Adapter extends RecyclerView.Adapter<StudentResult_Adapter.StudentDiary_ViewHolder> {

    Context mCtx;
    ArrayList<StudentResultclass> studentDiaryList;

    public StudentResult_Adapter(Context mCtx, ArrayList<StudentResultclass> studentDiaryList) {
        this.mCtx = mCtx;
        this.studentDiaryList = studentDiaryList;
    }

    @NonNull
    @Override
    public StudentDiary_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.studentresultlayout,parent,false);
        return new StudentResult_Adapter.StudentDiary_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDiary_ViewHolder holder, int position) {
        StudentResultclass setter = studentDiaryList.get(position);

               holder.grade.setText(setter.getGrade());
               holder.totalmarks.setText(setter.getTotalmarks());
               holder.obtainmarks.setText(setter.getMarks());
               holder.teachername.setText(setter.getTeachername());
               holder.subjectname.setText(setter.getSubjectname());
        //Toast.makeText(mCtx, "hjg"+setter.getSubjectname(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return studentDiaryList.size();
    }

    public class StudentDiary_ViewHolder extends RecyclerView.ViewHolder{
        TextView subjectname,teachername,obtainmarks,totalmarks,grade;

        public StudentDiary_ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectname = itemView.findViewById(R.id.textView31);
            teachername = itemView.findViewById(R.id.textView33);
            totalmarks= itemView.findViewById(R.id.textView35);
            obtainmarks = itemView.findViewById(R.id.textView37);
            grade=itemView.findViewById(R.id.textView39);

        }
    }
}
