package com.teacherstudent.Student;

import android.app.AlertDialog;
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

public class StudentDiary extends AppCompatActivity {

    ArrayList<StudentDiary_Setter> studentDiaryList = new ArrayList<>();
    StudentDiary_Adapter studentDiary_adapter;
    public AlertDialog alertDialog;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    RecyclerView recyclerView;

    String className,sectionName,studyGroup,subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_diary);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Diary");


        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        studentDiary_adapter = new StudentDiary_Adapter(this, studentDiaryList);// constructor
        recyclerView = findViewById(R.id.student_diary_rcyclrView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(studentDiary_adapter);

        Bundle extras = getIntent().getExtras();
        className = extras.getString("className");
        sectionName = extras.getString("sectionName");
        studyGroup = extras.getString("studyGroup");
        subjectName = extras.getString("subjectName");

        alertDialog = new SpotsDialog.Builder()
                .setContext(StudentDiary.this)
                .setMessage("Loading")
                .setCancelable(false)
                .build();
        alertDialog.show();

        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Classes")
                .child(className)
                .child(sectionName)
                .child("StudyGroup")
                .child(studyGroup)
                .child("Subjects")
                .child(subjectName)
                .child("Diaries");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentDiaryList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String diaryDate = dataSnapshot1.getKey();
                    String diaryTime = dataSnapshot1.child("diaryTime").getValue().toString();
                    String diaryDetail = dataSnapshot1.child("diaryDetail").getValue().toString();
                    String diarySenderEmail = dataSnapshot1.child("diarySenderEmail").getValue().toString();

                    Log.e("112233", "diary: " + diaryDate + diaryTime + diaryDetail + diarySenderEmail);

                    StudentDiary_Setter diary_setter = new StudentDiary_Setter(diaryDate, diaryTime, diarySenderEmail, diaryDetail);
                    studentDiaryList.add(diary_setter);
                    studentDiary_adapter.notifyDataSetChanged();

                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
