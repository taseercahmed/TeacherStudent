package com.teacherstudent;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class TimetableActivityTeacher extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter1 viewPagerAdapter;
    TabLayout tabLayout;
    RecyclerView recyclerView;
   // List<String> users;
  //  SearchView searchView;
   // Toolbar toolbar;
    String cname,se,gr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        getSupportActionBar().setTitle("Timetable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = findViewById(R.id.pages);
        viewPagerAdapter = new ViewPagerAdapter1(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        //  searchView = findViewById(R.id.searchview);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("MON");
        tabLayout.getTabAt(1).setText("TUE");
        tabLayout.getTabAt(2).setText("WED");
        tabLayout.getTabAt(3).setText("THUS");
        tabLayout.getTabAt(4).setText("FRI");


     //   ReadTeacherData();
        Intent intent=getIntent();
       String cname= intent.getStringExtra("className");
       String sec=intent.getStringExtra("sectionName");
        String grou=intent.getStringExtra("studyGroup");

        getClassNameFromIntent();
        getSectionNameFromIntent();
        getStudyGroupFromIntent();
        SampleTimetableclass.classname=cname;
        SampleTimetableclass.section=sec;
        SampleTimetableclass.group=grou;






    }
    public String getClassNameFromIntent() {
        Intent intent = getIntent();
        String className = intent.getStringExtra("className");
        return className;
    }

    public String getSectionNameFromIntent() {
        Intent intent = getIntent();
        String sectionName = intent.getStringExtra("sectionName");
        return sectionName;
    }

    public String getStudyGroupFromIntent() {
        Intent intent = getIntent();
        String studyGroup = intent.getStringExtra("studyGroup");
        return studyGroup;
    }

//    private void ReadTeacherData() {
//        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
//        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
//        databaseReference.child("Student")
//                .child(email.replace(".","_dot_"))
//                .child("ClassDetail")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                           cname=dataSnapshot.child("className").getValue().toString();
//                    //    Toast.makeText(TimetableActivity.this,classname,Toast.LENGTH_SHORT).show();
//                           se=dataSnapshot.child("section").getValue().toString();
//                           gr=dataSnapshot.child("studyGroup").getValue().toString();
//                        // SampleTimetableclass sampleTimetableclass=new SampleTimetableclass(classname,section,group);
//                             SampleTimetableclass.classname=cname;
//                             SampleTimetableclass.section=se;
//                             SampleTimetableclass.group=gr;
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//    }


}
