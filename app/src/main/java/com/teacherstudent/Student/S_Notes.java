package com.teacherstudent.Student;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
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

public class S_Notes extends AppCompatActivity {

    ArrayList<S_Notes_Setter> notesList = new ArrayList<>();
    S_Notes_Adapter notes_adapter;
    RecyclerView recyclerView;
    public AlertDialog alertDialog;
    String namess;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    String className,sectionName,studyGroup,subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_notes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notes");

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        notes_adapter = new S_Notes_Adapter(this, notesList);// constructor
        recyclerView = findViewById(R.id.S_notes_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notes_adapter);

        Bundle extras = getIntent().getExtras();
        className = extras.getString("className");
        sectionName = extras.getString("sectionName");
        studyGroup = extras.getString("studyGroup");
        subjectName = extras.getString("subjectName");

        alertDialog = new SpotsDialog.Builder()
                .setContext(S_Notes.this)
                .setMessage("Uploading")
                .setCancelable(false)
                .build();

        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Classes")
                .child(className)
                .child(sectionName)
                .child("StudyGroup")
                .child(studyGroup)
                .child("Subjects")
                .child(subjectName)
                .child("Notes");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notesList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String notesKey = dataSnapshot1.getKey();

                    String S_Notes_date = dataSnapshot1.child("documentDate").getValue().toString();
                    String S_Notes_messageDetail = dataSnapshot1.child("documentMessage").getValue().toString();
                    String S_Notes_senderEmail = dataSnapshot1.child("documentSenderEmail").getValue().toString();
                    String S_Notes_time = dataSnapshot1.child("documentTime").getValue().toString();
                    String S_Notes_msgType = dataSnapshot1.child("documentType").getValue().toString();
                    String subjectName = dataSnapshot1.child("subjectName").getValue().toString();
                    if (!TextUtils.isEmpty(S_Notes_messageDetail.toString())){
                        S_Notes_Setter notes_setter = new S_Notes_Setter(S_Notes_date,S_Notes_messageDetail,S_Notes_senderEmail,S_Notes_time,S_Notes_msgType);
                        notesList.add(notes_setter);
                        notes_adapter.notifyDataSetChanged();
                    }

                    Log.e("112233", "diary: " + S_Notes_date+S_Notes_messageDetail+S_Notes_senderEmail+S_Notes_time+S_Notes_msgType);
                  //   namess=Readname(S_Notes_senderEmail);


                }

                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
  //   String name="taseer ahmed";

}
