package com.teacherstudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Timetable extends RecyclerView.Adapter<Timetable.MyViewHolder> {

    private List<Timetableclass> list;
    private Context context;
    EventListener listener;

    public interface EventListener {
        void Editdata();
        void deleteTimeTable(String subjectname, String teachername);
    }

    public Timetable(List<Timetableclass> list, EventListener listener) {
        this.listener = listener;
        this.list=list;
    }

    public Timetable(List<Timetableclass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timetableresource_file, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Timetableclass model = list.get(position);

        //String subjectName,assignedTeacher,endTime,roomNo,startTime;
        final String className = model.getClassName();
        final String sectionName = model.getSectionName();
        final String studyGroup = model.getStudyGroup();

        final String subjectName = model.getSubjectName();
        final String assignedTeacher = model.getAssignedTeacher();
        String endTime = model.getEndTime();
        String roomNo = model.getRoomNo();
        String startTime = model.getStartTime();
      //  Toast.makeText(context,""+endTime+"  "+assignedTeacher+"  "+startTime+"  "+roomNo,Toast.LENGTH_SHORT).show();

        //TextView TT_timming, TT_subjectName, TT_subjectEmail, TT_roomNo;
        holder.TT_timming.setText(startTime + " - " + endTime);
        holder.TT_subjectName.setText(subjectName);
        holder.TT_teacherEmail.setText(assignedTeacher.replace("_dot_","."));
        holder.TT_roomNo.setText(roomNo);

//        holder.menu_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popup = new PopupMenu(context, holder.menu_btn);
//                //inflating menu from xml resource
//                popup.inflate(R.menu.editmenu);
//                //adding click listener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action_edit:
//
//                               // context.EditFun();
//                                listener.Editdata();
//
//
//
//
//
//
////                                //handle menu1 click
////                                Intent intent = new Intent(context, UpdateTimetable.class);
////                                intent.putExtra("classname",className);
////                                intent.putExtra("section",sectionName);
////                                intent.putExtra("studygroup",studyGroup);
////                                intent.putExtra("key",model.getKey());
////                                context.startActivity(intent);
//
//                                return true;
//                            case R.id.action_delete:
//                                //handle menu2 click
//                                listener.deleteTimeTable(subjectName,assignedTeacher);
//                                return true;
//
//                            default:
//                                return false;
//                        }
//                    }
//                });
//                //displaying the popup
//                popup.show();
//            }
//        });

    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TT_timming, TT_subjectName, TT_teacherEmail, TT_roomNo;
        ImageView menu_btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            TT_timming = itemView.findViewById(R.id.TT_timming);
            TT_subjectName = itemView.findViewById(R.id.TT_subjectName);
            TT_teacherEmail = itemView.findViewById(R.id.TT_teacherEmail);
            TT_roomNo = itemView.findViewById(R.id.TT_roomNo);

            menu_btn = itemView.findViewById(R.id.menu_btn);
        }
    }
}
