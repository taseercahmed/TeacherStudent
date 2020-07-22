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


public class StudentDiary_Adapter extends RecyclerView.Adapter<StudentDiary_Adapter.StudentDiary_ViewHolder> {

    Context mCtx;
    ArrayList<StudentDiary_Setter> studentDiaryList;

    public StudentDiary_Adapter(Context mCtx, ArrayList<StudentDiary_Setter> studentDiaryList) {
        this.mCtx = mCtx;
        this.studentDiaryList = studentDiaryList;
    }

    @NonNull
    @Override
    public StudentDiary_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_student_diary_rcyclrdesign,parent,false);
        return new StudentDiary_Adapter.StudentDiary_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentDiary_ViewHolder holder, int position) {
        StudentDiary_Setter setter = studentDiaryList.get(position);

        holder.SD_senderEmail.setText(setter.getSD_senderEmail());
        holder.SD_diaryDetail.setText(setter.getSD_diaryDetail());
        holder.SD_diaryTime.setText(setter.getSD_diaryTime());
        holder.SD_diaryDate.setText(setter.getSD_diaryDate());
    }

    @Override
    public int getItemCount() {
        return studentDiaryList.size();
    }

    public class StudentDiary_ViewHolder extends RecyclerView.ViewHolder{
        TextView SD_senderEmail,SD_diaryDetail,SD_diaryTime,SD_diaryDate;

        public StudentDiary_ViewHolder(@NonNull View itemView) {
            super(itemView);

            SD_senderEmail = itemView.findViewById(R.id.SD_senderEmail);
            SD_diaryDetail = itemView.findViewById(R.id.SD_diaryDetail);
            SD_diaryTime = itemView.findViewById(R.id.SD_diaryTime);
            SD_diaryDate = itemView.findViewById(R.id.SD_diaryDate);

        }
    }
}
