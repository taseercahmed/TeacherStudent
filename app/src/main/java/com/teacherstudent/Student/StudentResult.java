package com.teacherstudent.Student;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teacherstudent.R;
import com.teacherstudent.StudentResult_Adapter;
import com.teacherstudent.StudentResultclass;

import java.util.ArrayList;

public class StudentResult extends AppCompatActivity {
RecyclerView recyclerView;
ArrayList<StudentResultclass> list=new ArrayList<>();
StudentResult_Adapter studentSubjects_adapter;
TextView notfoundtext;
    private String className,sectionName,studyGroup,subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);
        Toolbar toolba=findViewById(R.id.toolbard);

        setSupportActionBar(toolba);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolba.setTitleTextColor(getResources().getColor(R.color.purewhite));
        getSupportActionBar().setTitle("Result");
        //  mStorageReference = FirebaseStorage.getInstance().getReference();


        toolba.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        notfoundtext=findViewById(R.id.textView40);
        recyclerView=findViewById(R.id.recd);
        studentSubjects_adapter = new StudentResult_Adapter(this, list);// constructor
      //  recyclerView = findViewById(R.id.student_subject_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(studentSubjects_adapter);
        readresult();
    }

    private void readresult() {
        Bundle extras = getIntent().getExtras();
        className = extras.getString("className");
        sectionName = extras.getString("sectionName");
        studyGroup = extras.getString("studyGroup");
        subjectName = extras.getString("subjectName");
        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Classes")
                .child(className)
                .child(sectionName)
                .child("StudyGroup")
                .child(studyGroup)
                .child("Result")
                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","_dot_"));
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
              for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                  if (dataSnapshot1.child("grade").exists()&&dataSnapshot1.child("marks").exists()&&dataSnapshot1.child("totalmarks").exists()){
                      String g=dataSnapshot1.child("grade").getValue().toString();
                      String m=dataSnapshot1.child("marks").getValue().toString();
                      String tm=dataSnapshot1.child("totalmarks").getValue().toString();
                      String teachername=dataSnapshot1.child("teachername").getValue().toString();
                      String subname=dataSnapshot1.child("subjectname").getValue().toString();
                      StudentResultclass studentResultclass=new StudentResultclass(subname,m,g,tm,teachername);
                      if (list.contains(studentResultclass)){

                      }else{
                          list.add(studentResultclass);
                      }


                  }
              }

                studentSubjects_adapter.notifyDataSetChanged();
              if (list.size()>0){
                  notfoundtext.setVisibility(View.GONE);
              }else{
                  notfoundtext.setVisibility(View.VISIBLE);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}