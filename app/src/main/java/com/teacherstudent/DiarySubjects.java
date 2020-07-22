package com.teacherstudent;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

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

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class DiarySubjects extends AppCompatActivity {

    ArrayList<DiarySubjects_Setter> subjects = new ArrayList<>();
    DiarySubjects_Adapter diarySubjects_adapter;
    public AlertDialog alertDialog;
    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    RecyclerView recyclerView;
    String nextActivityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_subjects);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select subject");

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        Bundle extras = getIntent().getExtras();
        nextActivityName = extras.getString("nextActivityName");

        diarySubjects_adapter = new DiarySubjects_Adapter(this, subjects);// constructor
        recyclerView = findViewById(R.id.diarySubjectsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(diarySubjects_adapter);


        alertDialog = new SpotsDialog.Builder()
                .setContext(DiarySubjects.this)
                .setMessage("Loading")
                .setCancelable(false)
                .build();
        alertDialog.show();

        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Teacher")
                .child(myEmail.replace(".","_dot_"))
                .child("TimeTable");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subjects.clear();

                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    final String classNumber = dataSnapshot1.getKey();

                    for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                        final String section = dataSnapshot2.getKey();

                        for (DataSnapshot dataSnapshot3:dataSnapshot2.getChildren()){
                            final String studyGroup = dataSnapshot3.getKey();

                            for (DataSnapshot dataSnapshot4:dataSnapshot3.getChildren()){
                                final String subjectName = dataSnapshot4.getKey();
                                //Log.e("112233",subjectName);

                                DiarySubjects_Setter diarySubjects_setter = new DiarySubjects_Setter(classNumber,section,studyGroup,subjectName);
                                subjects.add(diarySubjects_setter);
                            }
                        }
                    }
                }
                diarySubjects_adapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


    }

    public void openRelatedActivity(String className,String sectionName,String studyGroup,String subjectName){
        switch (nextActivityName){
            case "Diary":
                Intent intent1 = new Intent(DiarySubjects.this, Diary.class);
                intent1.putExtra("className", className);
                intent1.putExtra("sectionName", sectionName);
                intent1.putExtra("studyGroup", studyGroup);
                intent1.putExtra("subjectName", subjectName);
                startActivity(intent1);
                break;

            case "SentNotifications":
                Intent intent2 = new Intent(DiarySubjects.this, SentNotifications.class);
                intent2.putExtra("className", className);
                intent2.putExtra("sectionName", sectionName);
                intent2.putExtra("studyGroup", studyGroup);
                intent2.putExtra("subjectName", subjectName);
                startActivity(intent2);
                break;

            case "Notes":
                Intent intent3 = new Intent(DiarySubjects.this, Notes.class);
                intent3.putExtra("className", className);
                intent3.putExtra("sectionName", sectionName);
                intent3.putExtra("studyGroup", studyGroup);
                intent3.putExtra("subjectName", subjectName);
                startActivity(intent3);
                break;
            case "result":
                Intent intent4 = new Intent(DiarySubjects.this, TeacherResult.class);
                intent4.putExtra("className", className);
                intent4.putExtra("sectionName", sectionName);
                intent4.putExtra("studyGroup", studyGroup);
                intent4.putExtra("subjectName", subjectName);
                startActivity(intent4);
                break;
        }

    }


}
