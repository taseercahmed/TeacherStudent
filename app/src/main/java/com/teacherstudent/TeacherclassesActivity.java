package com.teacherstudent;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class TeacherclassesActivity extends AppCompatActivity {
    public AlertDialog alertDialog;
    RecyclerView recyclerView;
    List<ClassSectionGroup> list = new ArrayList<>();

    FloatingActionButton TF_floatingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherclasses);
     //   TF_floatingBtn = findViewById(R.id.TF_floatingBtn);

        alertDialog = new SpotsDialog.Builder()
                .setContext(TeacherclassesActivity.this)
                .setMessage("Loading")
                .setCancelable(false)
                .build();
        alertDialog.show();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Time Table");





//
//        timeTable_adapter = new TimeTable_Adapter(this, subjectsList);// constructor
        recyclerView = findViewById(R.id.timeTableRecyclrView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        // selectClass();
        showClasses();



//        TF_floatingBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             //   startActivity(new Intent(TimetableFirstActivity.this, CreateTimetable.class));
//            }
//        });

    }

    private void showClasses() {
        // final String classNumber;
        //DatabaseReference databaseReference:FirebaseDatabase.getInstance().getReference();
        final Show_timetable_Adapter show_timetable_adapter = new Show_timetable_Adapter(TeacherclassesActivity.this, list);
        recyclerView.setAdapter(show_timetable_adapter);
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
     //   Toast.makeText(TeacherclassesActivity.this,"email is "+email.replace(".","_dot_"),Toast.LENGTH_SHORT).show();

        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Teacher")
                .child(email.replace(".","_dot_"))
                .child("TimeTable");

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String className = dataSnapshot1.getKey();
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        final String sectionName = dataSnapshot2.getKey();

                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                            final String studyGroup = dataSnapshot3.getKey();

                            ClassSectionGroup classSectionGroup = new ClassSectionGroup(className, sectionName, studyGroup);
                            list.add(classSectionGroup);

                        }
                    }
                }
                show_timetable_adapter.notifyDataSetChanged();
                alertDialog.dismiss();
           //     Toast.makeText(TeacherclassesActivity.this,"list is "+list.size(),Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
