package com.teacherstudent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class ActivityTeacher extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    TextView drawer_email,drawer_uName;
    CircleImageView drawer_uImage;
    ImageView drawer_edit;
    NavigationView nav_view;

    TextView textCartItemCount;
    int mCartItemCount = 0;
    View actionView;

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
        setContentView(R.layout.activity_teacher);

        //fcmNotification
        tokenRegister();

        nav_view = findViewById(R.id.nav_view);
        LayoutInflater.from(this).inflate(R.layout.header_nav, nav_view);

        toolbar=findViewById(R.id.toolbar);
        drawer_email = findViewById(R.id.drawer_email);
        drawer_uImage = findViewById(R.id.drawer_uImage);
        drawer_uName = findViewById(R.id.drawer_uName);
        drawer_edit = findViewById(R.id.drawer_edit);

        toolbar.setTitle("Teacher");
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        NavigationView view=findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close
        );
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view.setNavigationItemSelectedListener(this);


        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        drawer_email.setText(myEmail);



        activityTeacher_adapter = new ActivityTeacher_Adapter(this, eventList);// constructor
        recyclerView = findViewById(R.id.TA_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(activityTeacher_adapter);


        alertDialog = new SpotsDialog.Builder()
                .setContext(ActivityTeacher.this)
                .setMessage("Please wait")
                .setCancelable(false)
                .build();
        alertDialog.show();





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
                            //Log.e("1122334","events"+dataSnapshot2);
                            String eventFor = dataSnapshot2.child("eventFor").getValue().toString();

                            if (eventFor.contains("teacher")) {

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



        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Teacher")
                .child(myEmail.replace(".","_dot_"))
                .child("Profile");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    drawer_uName.setText(name);
                }
                if (dataSnapshot.child("profileImageLink").exists()){
                    String imageLink = dataSnapshot.child("profileImageLink").getValue().toString();
                    Glide.with(getApplicationContext())
                            .load(imageLink)
                            .into(drawer_uImage);
                }
                alertDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });




//        SliderView sliderView = findViewById(R.id.imageSlider);
//        SliderAdapterExample adapter = new SliderAdapterExample(this);
//        sliderView.setSliderAdapter(adapter);
//        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        //sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        /*sliderView.setIndicatorSelectedColor(Color.WHITE);
//        sliderView.setIndicatorUnselectedColor(Color.GRAY);*/
//        sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
//        sliderView.startAutoCycle();


        drawer_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityTeacher.this,ProfileTeacher.class));
            }
        });

        badgeCounter();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();
        switch (id){
            case R.id.result:
                Intent intent6 = new Intent(ActivityTeacher.this,DiarySubjects.class);
                intent6.putExtra("nextActivityName","result");
                startActivity(intent6);
                break;

            case R.id.nav_timetable:
                startActivity(new Intent(ActivityTeacher.this,TeacherclassesActivity.class));
                break;

            case R.id.nav_diaries:
                Intent intent1 = new Intent(ActivityTeacher.this,DiarySubjects.class);
                intent1.putExtra("nextActivityName","Diary");
                startActivity(intent1);
                break;

            case R.id.nav_notes:
                Intent intent3 = new Intent(ActivityTeacher.this,DiarySubjects.class);
                intent3.putExtra("nextActivityName","Notes");
                startActivity(intent3);
                break;

            case R.id.nav_sent_notifications:
                Intent intent2 = new Intent(ActivityTeacher.this,DiarySubjects.class);
                intent2.putExtra("nextActivityName","SentNotifications");
                startActivity(intent2);
                break;

            case R.id.nav_reset_password:
                resetPasswordDialog();
                break;

            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String shareBody = "University management app download now. https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
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
                startActivity(new Intent(ActivityTeacher.this,MainActivity.class));
                finish();
                break;
        }

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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

                            final AlertDialog.Builder builder2 = new AlertDialog.Builder(ActivityTeacher.this);
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
                            Toast.makeText(ActivityTeacher.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);

        final MenuItem notification = menu.findItem(R.id.action_student_notifications);

        notification.setVisible(true);

        actionView = MenuItemCompat.getActionView(notification);
        textCartItemCount = (TextView) actionView.findViewById(R.id.SN_badgeCounter);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(notification);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_student_notifications: {
                startActivity(new Intent(ActivityTeacher.this, ReceivedNotifications.class));
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
                .child("Teacher")
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
                        //Log.e("112233","count: "+mCartItemCount);
                        setupBadge();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }

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
