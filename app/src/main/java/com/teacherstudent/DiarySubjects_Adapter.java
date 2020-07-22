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

public class DiarySubjects_Adapter extends RecyclerView.Adapter<DiarySubjects_Adapter.DiarySubjects_ViewHolder>{

    Context mCtx;
    ArrayList<DiarySubjects_Setter> subjects;

    public DiarySubjects_Adapter(Context mCtx, ArrayList<DiarySubjects_Setter> subjects) {
        this.mCtx = mCtx;
        this.subjects = subjects;
    }

    @NonNull
    @Override
    public DiarySubjects_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_diary_subjects_ryclr_design,parent,false);
        return new DiarySubjects_Adapter.DiarySubjects_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiarySubjects_ViewHolder holder, int position) {
        DiarySubjects_Setter diarySubjects_setter = subjects.get(position);
        final String subjectName = diarySubjects_setter.getDiarySubjectName();
        final String className = diarySubjects_setter.getDiaryClassName();
        final String sectionName = diarySubjects_setter.getDiarySectionName();
        final String studyGroup = diarySubjects_setter.getDiaryStudyGroup();

        holder.subjectNameForDiary.setText(subjectName);
        holder.classSectionStudyGroup.setText(className+" / "+sectionName+" / "+studyGroup);
        holder.DiarySubjectsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DiarySubjects)mCtx).openRelatedActivity(className,sectionName,studyGroup,subjectName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class DiarySubjects_ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameForDiary,classSectionStudyGroup;
        CardView DiarySubjectsCardView;
        public DiarySubjects_ViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectNameForDiary = itemView.findViewById(R.id.subjectNameForDiary);
            classSectionStudyGroup = itemView.findViewById(R.id.classSectionStudyGroup);
            DiarySubjectsCardView = itemView.findViewById(R.id.DiarySubjectsCardView);
        }
    }

}
