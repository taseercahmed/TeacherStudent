package com.teacherstudent.Student;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teacherstudent.R;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class StudentSubjects extends AppCompatActivity {

    ArrayList<StudentSubjects_Setter> subjectNamesList = new ArrayList<>();
    StudentSubjects_Adapter studentSubjects_adapter;
    RecyclerView recyclerView;
    public AlertDialog alertDialog;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    String nextActivityName;
    String className,sectionName,studyGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subjects);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Subjects");


        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        Bundle extras = getIntent().getExtras();
        nextActivityName = extras.getString("nextActivityName");


        studentSubjects_adapter = new StudentSubjects_Adapter(this, subjectNamesList);// constructor
        recyclerView = findViewById(R.id.student_subject_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(studentSubjects_adapter);


        alertDialog = new SpotsDialog.Builder()
                .setContext(StudentSubjects.this)
                .setMessage("Loading")
                .setCancelable(false)
                .build();
        alertDialog.show();


        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Student")
                .child(myEmail.replace(".","_dot_"))
                .child("ClassDetail");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              //  className = dataSnapshot.child("className").getValue().toString();
              //  sectionName = dataSnapshot.child("section").getValue().toString();
              //  studyGroup = dataSnapshot.child("studyGroup").getValue().toString();
                className=dataSnapshot.child("className").getValue().toString();
                //    Toast.makeText(TimetableActivity.this,classname,Toast.LENGTH_SHORT).show();
                sectionName=dataSnapshot.child("sectionName").getValue().toString();
                studyGroup=dataSnapshot.child("studyGroup").getValue().toString();

                showSubjects(className,sectionName,studyGroup);

                alertDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void showSubjects(String classname, String section, String studyGroup) {
        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Classes")
                .child(classname)
                .child(section)
                .child("StudyGroup")
                .child(studyGroup)
                .child("SubjectsName");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjectNamesList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String subject_name = dataSnapshot1.getKey();
                    Log.e("112233", "subjects: " + subject_name);

                    StudentSubjects_Setter studentSubjects_setter = new StudentSubjects_Setter(subject_name);
                    subjectNamesList.add(studentSubjects_setter);
                    studentSubjects_adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void openRelatedActivity(String subjectName){
        switch (nextActivityName){
            case "Diary":
                Intent intent1 = new Intent(StudentSubjects.this, StudentDiary.class);
                intent1.putExtra("className", className);
                intent1.putExtra("sectionName", sectionName);
                intent1.putExtra("studyGroup", studyGroup);
                intent1.putExtra("subjectName", subjectName);
                startActivity(intent1);
                break;

            case "Notes":
                Intent intent3 = new Intent(StudentSubjects.this, S_Notes.class);
                intent3.putExtra("className", className);
                intent3.putExtra("sectionName", sectionName);
                intent3.putExtra("studyGroup", studyGroup);
                intent3.putExtra("subjectName", subjectName);
                startActivity(intent3);
                break;

            /*case "SentNotifications":
                Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(StudentSubjects.this, SentNotifications.class);
                intent2.putExtra("className", className);
                intent2.putExtra("sectionName", sectionName);
                intent2.putExtra("studyGroup", studyGroup);
                intent2.putExtra("subjectName", subjectName);
                startActivity(intent2);
                break;*/


        }

    }
}
