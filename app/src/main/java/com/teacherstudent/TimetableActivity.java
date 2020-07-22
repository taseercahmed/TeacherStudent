package com.teacherstudent;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TimetableActivity extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    RecyclerView recyclerView;
    List<String> users;
    SearchView searchView;
    Toolbar toolbar;
    String cname,se,gr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

      //  toolbar = findViewById(R.id.toolbarid);
       // setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Timetable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(TimetableActivity.this, StudentMainActivity.class));
//            }
//        });

        viewPager = findViewById(R.id.pages);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        //  searchView = findViewById(R.id.searchview);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("MON");
        tabLayout.getTabAt(1).setText("TUE");
        tabLayout.getTabAt(2).setText("WED");
        tabLayout.getTabAt(3).setText("THUS");
        tabLayout.getTabAt(4).setText("FRI");

        Intent intent=getIntent();
        String cname=intent.getStringExtra("cname");
        String se=intent.getStringExtra("se");
        String gr=intent.getStringExtra("gr");
        SampleTimetableclass.classname=cname;
        SampleTimetableclass.section=se;
        SampleTimetableclass.group=gr;


        ReadStudentData();

        // tabLayout.getTabAt(4).setText("FRI");
        // tabLayout.getTabAt(5).setText("SAT");
        // tabLayout.getTabAt(4).setText("FRI");
        //  tabLayout.getTabAt(5).setText("SAT");

    }

    private void ReadStudentData() {
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Student")
                .child(email.replace(".","_dot_"))
                .child("ClassDetail")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                           cname=dataSnapshot.child("className").getValue().toString();
                    //    Toast.makeText(TimetableActivity.this,classname,Toast.LENGTH_SHORT).show();
                           se=dataSnapshot.child("sectionName").getValue().toString();
                           gr=dataSnapshot.child("studyGroup").getValue().toString();
                        // SampleTimetableclass sampleTimetableclass=new SampleTimetableclass(classname,section,group);


                            //   Toast.makeText(TimetableActivity.this,"classname is "+classname+"  "+section+"   "+group,Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {



                    }
                });

    }

//    public String getClassNameFromIntent() {
//       // Intent intent = getIntent();
//        String className=classname;
//        return classname;
//    }
//
//    public String getSectionNameFromIntent() {
//     //   Intent intent = getIntent();
//        String sectionName=section;
//        return section;
//    }
//
//    public String getStudyGroupFromIntent() {
//  //      Intent intent = getIntent();
//        String studyGroup = group;
//        return group;
//    }
}
