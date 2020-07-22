package com.teacherstudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Diary_Adapter extends RecyclerView.Adapter<Diary_Adapter.Diary_ViewHolder> {

    Context mCtx;
    ArrayList<Diary_Setter> diaryList;

    public Diary_Adapter(Context mCtx, ArrayList<Diary_Setter> diaryList) {
        this.mCtx = mCtx;
        this.diaryList = diaryList;
    }

    @NonNull
    @Override
    public Diary_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_diary_ryclr_design,parent,false);
        return new Diary_Adapter.Diary_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Diary_ViewHolder holder, int position) {
        Diary_Setter diary_setter = diaryList.get(position);

        String diaryDate = diary_setter.getDiaryDate();
        String diaryDetail = diary_setter.getDiaryDetail();
        String diaryTime = diary_setter.getDiaryTime();

        holder.diaryDate.setText(diaryDate);
        holder.diaryDetail.setText(diaryDetail);
        holder.diaryTime.setText(diaryTime);
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public class Diary_ViewHolder extends RecyclerView.ViewHolder{
        TextView diaryDate,diaryDetail,diaryTime;

        public Diary_ViewHolder(@NonNull View itemView) {
            super(itemView);

            diaryDate = itemView.findViewById(R.id.diaryDate);
            diaryDetail = itemView.findViewById(R.id.diaryDetail);
            diaryTime = itemView.findViewById(R.id.diaryTime);

        }
    }





    /*public void selectAllItem(boolean isSelectedAll) {
        try {
            if (diaryList != null) {
                for (int index = 0; index < diaryList.size(); index++) {
                    diaryList.get(index).setSelected(isSelectedAll);
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



}
