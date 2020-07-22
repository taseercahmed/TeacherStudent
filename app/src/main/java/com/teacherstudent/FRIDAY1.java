package com.teacherstudent;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FRIDAY1 extends Fragment {
    RecyclerView recyclerView;
    List<Timetableclass> timetableclasses;
    String classname, section,studyGroup;
    Context contxt;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contxt = context;
    }

    public FRIDAY1() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friday1, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewfortimetable);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(contxt);
        recyclerView.setLayoutManager(linearLayoutManager);
        timetableclasses = new ArrayList<>();
        SampleTimetableclass sampleTimetableclass=new SampleTimetableclass();

//        classname = sampleTimetableclass.getClassname();
//        section=sampleTimetableclass.getSection();
//        studyGroup=sampleTimetableclass.getGroup();


        ReadTimetable();

        // Inflate the layout for this fragment
        return view;
    }

    private void ReadTimetable() {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final Timetable timetable = new Timetable(timetableclasses, contxt);
        recyclerView.setAdapter(timetable);

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Teacher")
                .child(email.replace(".","_dot_"))
                .child("OriginalTimeTable")
                .child(SampleTimetableclass.classname)
                .child(SampleTimetableclass.section)

                .child(SampleTimetableclass.group)
                .child("TimeTable")
                .child("Friday");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timetableclasses.clear();

                //  Toast.makeText(contxt, skey, Toast.LENGTH_SHORT).show();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String skey=dataSnapshot1.getKey();
                    String starttime=dataSnapshot1.child("startTime").getValue().toString();
                    String lasttime=dataSnapshot1.child("endTime").getValue().toString();
                    String teachername=dataSnapshot1.child("assignedTeacher").getValue().toString();
                    //  String subjectname = dataSnapshot1.child("subjectname").getValue().toString();
                    String room = dataSnapshot1.child("roomNo").getValue().toString();

                    //   String classname = dataSnapshot1.child("classname").getValue().toString();
                    //  String classsection = dataSnapshot1.child("classsection").getValue().toString();
                    //  String groupname = dataSnapshot1.child("studygroup").getValue().toString();
                    //  String key = dataSnapshot1.child("key").getValue().toString();
                    Timetableclass timetableclass = new Timetableclass(skey, teachername, lasttime, room, starttime);
                    timetableclasses.add(timetableclass);
                    //  Toast.makeText(contxt,"size is "+timetableclass.getStartTime(),Toast.LENGTH_SHORT).show();

                }
                // Toast.makeText(contxt,"size is "+timetableclasses.size(),Toast.LENGTH_SHORT).show();
                timetable.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
