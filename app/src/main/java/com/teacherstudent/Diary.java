package com.teacherstudent;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class Diary extends AppCompatActivity {

    ArrayList<Diary_Setter> diaryList = new ArrayList<>();
    Diary_Adapter diary_adapter;
    public AlertDialog alertDialog;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    RecyclerView recyclerView;

    String className,sectionName,studyGroup,subjectName;
    FloatingActionButton writeDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Diary");

        writeDiary = findViewById(R.id.writeDiaryFloatingBtn);

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        diary_adapter = new Diary_Adapter(this, diaryList);// constructor
        recyclerView = findViewById(R.id.diaryListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(diary_adapter);

        Bundle extras = getIntent().getExtras();
        className = extras.getString("className");
        sectionName = extras.getString("sectionName");
        studyGroup = extras.getString("studyGroup");
        subjectName = extras.getString("subjectName");

        Log.e("112233", "extraValue:" + className + sectionName + studyGroup + subjectName);

        alertDialog = new SpotsDialog.Builder()
                .setContext(Diary.this)
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
                diaryList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String diaryDate = dataSnapshot1.getKey();
                    String diaryTime = dataSnapshot1.child("diaryTime").getValue().toString();
                    String diaryDetail = dataSnapshot1.child("diaryDetail").getValue().toString();

                    Log.e("112233", "diary: " + diaryDate + diaryTime + diaryDetail);

                    Diary_Setter diary_setter = new Diary_Setter(diaryDate, diaryTime, diaryDetail);
                    diaryList.add(diary_setter);
                    diary_adapter.notifyDataSetChanged();

                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        writeDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Diary.this, WriteDiary.class);
                i.putExtra("className", className);
                i.putExtra("sectionName", sectionName);
                i.putExtra("studyGroup", studyGroup);
                i.putExtra("subjectName", subjectName);
                startActivity(i);


                /*for (int i = 0; i < diaryList.size(); i++) {
                    if (diaryList.get(i).isSelected()) {
                        flagSelectAll = true;
                        diary_adapter.selectAllItem(false);
                        diary_adapter.notifyDataSetChanged();
                    } else {
                        diary_adapter.selectAllItem(true);
                        diary_adapter.notifyDataSetChanged();
                    }
                    break;
                }*/
            }
        });
    }
    boolean flagSelectAll = false;

}
