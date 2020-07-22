package com.teacherstudent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WriteNotification_Adapter extends RecyclerView.Adapter<WriteNotification_Adapter.WriteNotification_ViewHolder> {

    Boolean isSelectedAll=true;
    Boolean allBtn = true;
    WriteNotification writeNotification = new WriteNotification();
    static ArrayList selectedStudentList = new ArrayList();

    Context mCtx;
    ArrayList<WriteNotification_Setter> studentList;

    public WriteNotification_Adapter(Context mCtx, ArrayList<WriteNotification_Setter> studentList) {
        this.mCtx = mCtx;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public WriteNotification_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_write_notification_ryclr_design,parent,false);
        return new WriteNotification_Adapter.WriteNotification_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WriteNotification_ViewHolder holder, int position) {
        final WriteNotification_Setter writeNotification_setter = studentList.get(position);
        String WN_studentsEmail = writeNotification_setter.getWN_studentsEmail();

        holder.selectEmailCheckBox.setText(WN_studentsEmail.replace("_dot_","."));

        if (allBtn) {
            if (isSelectedAll) {
                holder.selectEmailCheckBox.setChecked(true);
                selectedStudentList.add(writeNotification_setter.getWN_studentsEmail());
                Log.e("112233",selectedStudentList.toString());
            }
            if (!isSelectedAll) {
                holder.selectEmailCheckBox.setChecked(false);
                selectedStudentList.clear();
                Log.e("112233","unselectedAll"+selectedStudentList.toString());
            }
        }

        holder.selectEmailCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn=false;
                writeNotification.selectAll.setChecked(false);
                if (holder.selectEmailCheckBox.isChecked()){
                    selectedStudentList.add(writeNotification_setter.getWN_studentsEmail());
                }else {
                    selectedStudentList.remove(writeNotification_setter.getWN_studentsEmail());
                }
                Log.e("112233",selectedStudentList.toString());
                notifyDataSetChanged();
            }
        });

        if (selectedStudentList.size()==studentList.size()){
            writeNotification.selectAll.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class WriteNotification_ViewHolder extends RecyclerView.ViewHolder {
        CheckBox selectEmailCheckBox;

        public WriteNotification_ViewHolder(@NonNull View itemView) {
            super(itemView);

            selectEmailCheckBox = itemView.findViewById(R.id.selectEmailCheckBox);
        }
    }



    public void selectAll(){
        allBtn=true;
        isSelectedAll=true;
        notifyDataSetChanged();
    }
    public void unselectall(){
        allBtn=true;
        isSelectedAll=false;
        notifyDataSetChanged();
    }


}
