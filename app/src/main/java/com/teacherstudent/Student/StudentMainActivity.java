package com.teacherstudent.Student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teacherstudent.ActivityTeacher_Adapter;
import com.teacherstudent.ActivityTeacher_Setter;
import com.teacherstudent.Feechallan;
import com.teacherstudent.MainActivity;
import com.teacherstudent.R;
import com.teacherstudent.SampleTimetableclass;
import com.teacherstudent.TimetableActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class StudentMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    NavigationView nav_view;
    Toolbar toolbar;
    TextView student_drawer_email,student_drawer_name;
    CircleImageView student_drawer_uImage;

    String myEmail;
    String myName;
    FirebaseAuth mAuth;
    FirebaseUser fu;


    TextView textCartItemCount;
    int mCartItemCount = 0;
    View actionView;
    String cname="";
            String se="";
            String gr="";

    ArrayList<ActivityTeacher_Setter> eventList = new ArrayList<>();
    ActivityTeacher_Adapter activityTeacher_adapter;
    RecyclerView recyclerView;
    public AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_student_main);


        tokenRegister();


        nav_view = findViewById(R.id.student_navView);
        LayoutInflater.from(this).inflate(R.layout.student_header_nav, nav_view);

        toolbar=findViewById(R.id.student_toolbar);
        student_drawer_email = findViewById(R.id.student_drawer_email);
        student_drawer_uImage = findViewById(R.id.student_drawer_uImage);
        student_drawer_name = findViewById(R.id.student_drawer_name);
        //drawer_edit = findViewById(R.id.drawer_edit);

        toolbar.setTitle("Student");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        DrawerLayout drawerLayout=findViewById(R.id.student_drawer_layout);
        NavigationView view=findViewById(R.id.student_navView);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        ReadStudentData();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view.setNavigationItemSelectedListener(this);



        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();
        myName = fu.getDisplayName();



        student_drawer_email.setText(myEmail);
        student_drawer_name.setText(myName);



        student_drawer_uImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentMainActivity.this,S_StudentProfile.class));
            }
        });



        activityTeacher_adapter = new ActivityTeacher_Adapter(this, eventList);// constructor
        recyclerView = findViewById(R.id.TA_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(activityTeacher_adapter);



        alertDialog = new SpotsDialog.Builder()
                .setContext(StudentMainActivity.this)
                .setMessage("Please wait")
                .setCancelable(false)
                .build();
        alertDialog.show();

        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Student")
                .child(myEmail.replace(".","_dot_"))
                .child("Profile");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    student_drawer_name.setText(name);
                }
                if (dataSnapshot.child("profileImageLink").exists()){
                    String imageLink = dataSnapshot.child("profileImageLink").getValue().toString();
                    Glide.with(getApplicationContext())
                            .load(imageLink)
                            .into(student_drawer_uImage);
                }
                alertDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });



        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Events");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String eventDate = dataSnapshot1.getKey();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date strDate = null;
                    try {
                        strDate = sdf.parse(eventDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (System.currentTimeMillis() <= strDate.getTime()) {

                        for (DataSnapshot dataSnapshot2:dataSnapshot1.getChildren()){
                            String eventFor = dataSnapshot2.child("eventFor").getValue().toString();

                            if (eventFor.contains("student")) {

                                String eventTitle = dataSnapshot2.child("eventTitle").getValue().toString();
                                String eventDesciption = dataSnapshot2.child("eventDescription").getValue().toString();
                                String eventStartTime = dataSnapshot2.child("eventStartTime").getValue().toString();
                                String eventEndTime = dataSnapshot2.child("eventEndTime").getValue().toString();

                                ActivityTeacher_Setter setter = new ActivityTeacher_Setter(eventDate, eventTitle, eventDesciption, eventStartTime, eventEndTime);
                                eventList.add(setter);

                            }
                        }

                    }

                }
                activityTeacher_adapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        badgeCounter();



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        switch (id){
            case R.id.result:
                Intent intent8=new Intent(StudentMainActivity.this, StudentResult.class);
                intent8.putExtra("className",cname);
                intent8.putExtra("studyGroup",gr);
                intent8.putExtra("sectionName",se);
                startActivity(intent8);
                break;
            case R.id.fees:
             if (TextUtils.isEmpty(cname) && TextUtils.isEmpty(gr)&& TextUtils.isEmpty(se)){
                 Toast.makeText(this, "Check Network problem", Toast.LENGTH_SHORT).show();
             }else{
                 Intent intent7=new Intent(StudentMainActivity.this, Feechallan.class);
                 intent7.putExtra("className",cname);
                 intent7.putExtra("studyGroup",gr);
                 intent7.putExtra("sectionName",se);
                 startActivity(intent7);
             }
                break;
            case R.id.student_nav_home:
                break;

            case R.id.student_nav_timetable:
               Intent intent6=new Intent(StudentMainActivity.this, TimetableActivity.class);
                intent6.putExtra("cname",cname);
                intent6.putExtra("gr",gr);
                intent6.putExtra("se",se);
                startActivity(intent6);
                break;

            case R.id.student_nav_diaries:
                Intent intent1 = new Intent(StudentMainActivity.this, StudentSubjects.class);
                intent1.putExtra("nextActivityName","Diary");
                startActivity(intent1);
                break;

            case R.id.student_nav_notes:
                Intent intent2 = new Intent(StudentMainActivity.this,StudentSubjects.class);
                intent2.putExtra("nextActivityName","Notes");
                startActivity(intent2);
                break;

            case R.id.student_nav_notification:
                Intent intent3 = new Intent(StudentMainActivity.this,S_Notifications.class);
                startActivity(intent3);
                break;

            case R.id.student_nav_reset_password:
                resetPasswordDialog();
                break;

            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String shareBody = "School management app download now. https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share via"));
                break;

            case R.id.about_me:
                Uri uri = Uri.parse("http://newjhelum.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;


            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(StudentMainActivity.this, MainActivity.class));
                finish();
                break;
        }

        DrawerLayout drawerLayout=findViewById(R.id.student_drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
                        SampleTimetableclass.classname=cname;
                        SampleTimetableclass.section=se;
                        SampleTimetableclass.group=gr;

                        //   Toast.makeText(TimetableActivity.this,"classname is "+classname+"  "+section+"   "+group,Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {



                    }
                });

    }


    public void resetPasswordDialog(){
        Button teacher_resetBtn;
        final TextInputEditText teacher_resetEmail;

        LayoutInflater factory = LayoutInflater.from(this);
        final View notificationView = factory.inflate(R.layout.activity_teacher_resetpassword, null);


        teacher_resetBtn = notificationView.findViewById(R.id.teacher_resetBtn);
        teacher_resetEmail = notificationView.findViewById(R.id.teacher_resetEmail);

        final AlertDialog notificationDialog = new AlertDialog.Builder(this).create();
        notificationDialog.setView(notificationView);

        teacher_resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = teacher_resetEmail.getText().toString();
                if(!TextUtils.isEmpty(Email)){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            final AlertDialog.Builder builder2 = new AlertDialog.Builder(StudentMainActivity.this);
                            builder2.setTitle("Done");
                            builder2.setMessage("Check Your E-mail.");
                            builder2.setCancelable(false);
                            builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    notificationDialog.cancel();
                                }
                            });
                            AlertDialog alert12 = builder2.create();
                            alert12.show();

                            //Toast.makeText(MainActivity.this,"Successful, Check your Email",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(StudentMainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    teacher_resetEmail.setError("Empty");
                }
            }
        });
        notificationDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_student_notifications);

        menuItem.setVisible(true);

        actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.SN_badgeCounter);

        setupBadge();
        //textCartItemCount.setText("5");
        //badgeCounter();


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_student_notifications: {
                startActivity(new Intent(StudentMainActivity.this,S_Notifications.class));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(mCartItemCount));
                //textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void badgeCounter(){
        alertDialog.show();

        final DatabaseReference dbr1 = FirebaseDatabase.getInstance().getReference()
                .child("Student")
                .child(myEmail.replace(".","_dot_"))
                .child("Notification")
                .child("Admin");
        dbr1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCartItemCount = 0;
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                    String count = dataSnapshot1.child("notify").getValue().toString();
                    if (count.contains("true")){
                        mCartItemCount++;
                        setupBadge();
                    }
                }


                final DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference()
                        .child("Student")
                        .child(myEmail.replace(".","_dot_"))
                        .child("Notification")
                        .child("Teacher");
                dbr2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                            String count = dataSnapshot2.child("notify").getValue().toString();
                            if (count.contains("true")){
                                mCartItemCount++;
                                setupBadge();
                                alertDialog.dismiss();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    // fcmnotification
    public void tokenRegister(){
//        Log.d("112233", "token "+ FirebaseInstanceId.getInstance().getToken());
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String refreshToken = FirebaseInstanceId.getInstance().getToken();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
//        Token token = new Token(refreshToken);
//        Toast.makeText(this, ""+token, Toast.LENGTH_SHORT).show();
//        reference.child(firebaseUser.getEmail().replace(".","_dot_")).setValue(token);
    }
}
