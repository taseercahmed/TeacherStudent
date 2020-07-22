package com.teacherstudent.Student;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.robertlevonyan.views.expandable.Expandable;
import com.teacherstudent.R;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import me.grantland.widget.AutofitHelper;

public class S_Notifications extends AppCompatActivity {
    Expandable expandable,expandable2;
    TextView adminNotificationCounter,teacherNotificationCounter;

    ArrayList<S_Notifications_AdminAdapter_Setter> adminNotificationList = new ArrayList<>();
    S_Notifications_AdminAdapter s_notifications_adminAdapter;
    ArrayList<S_Notifications_AdminAdapter_Setter> teacherNotificationList = new ArrayList<>();
    S_Notifications_TeacherAdapter s_notifications_teacherAdapter;

    RecyclerView recyclerView1,recyclerView2;
    public AlertDialog alertDialog;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        adminNotificationCounter = findViewById(R.id.adminNotificationCounter);
        teacherNotificationCounter = findViewById(R.id.teacherNotificationCounter);
        expandable = findViewById(R.id.expandable);
        expandable2 = findViewById(R.id.expandable2);
        expandable.setAnimateExpand(true);
        expandable2.setAnimateExpand(true);


        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        s_notifications_adminAdapter = new S_Notifications_AdminAdapter(this, adminNotificationList);// constructor
        recyclerView1 = findViewById(R.id.content_SN_adminRcyclrView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView1.setLayoutManager(linearLayoutManager);
        recyclerView1.setAdapter(s_notifications_adminAdapter);




        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        myRecyclerView.setLayoutManager(linearLayoutManager);*/

        alertDialog = new SpotsDialog.Builder()
                .setContext(S_Notifications.this)
                .setMessage("Please wait")
                .setCancelable(false)
                .build();
        alertDialog.show();

        final DatabaseReference dbr1 = FirebaseDatabase.getInstance().getReference()
                .child("Student")
                .child(myEmail.replace(".","_dot_"))
                .child("Notification")
                .child("Admin");
        dbr1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adminNotificationList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                    String SN_date = dataSnapshot1.child("date").getValue().toString();
                    String SN_message = dataSnapshot1.child("message").getValue().toString();
                    String SN_time = dataSnapshot1.child("time").getValue().toString();
                    String SN_senderEmail = dataSnapshot1.child("senderEmail").getValue().toString();
                    String SN_senderName = dataSnapshot1.child("senderEmail").getValue().toString();
                    String SN_notify = dataSnapshot1.child("notify").getValue().toString();
                    String SN_key = dataSnapshot1.getKey();

                    Log.e("112233","value: "+SN_date+SN_message+SN_time);
                    S_Notifications_AdminAdapter_Setter s_notifications_adminAdapter_setter = new S_Notifications_AdminAdapter_Setter(
                            SN_date,SN_message,SN_senderEmail,SN_senderName,SN_time,SN_notify,SN_key);
                    adminNotificationList.add(s_notifications_adminAdapter_setter);
                }
                //Collections.reverse(adminNotificationList);
                s_notifications_adminAdapter.notifyDataSetChanged();
                adminNotificationCounter.setText(String.valueOf(adminNotificationList.size()));
                alertDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });



        s_notifications_teacherAdapter = new S_Notifications_TeacherAdapter(this, teacherNotificationList);// constructor
        recyclerView2 = findViewById(R.id.content_SN_teacherRcyclrView);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(linearLayoutManager1);
        recyclerView2.setAdapter(s_notifications_teacherAdapter);

        final DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference()
                .child("Student")
                .child(myEmail.replace(".","_dot_"))
                .child("Notification")
                .child("Teacher");
        dbr2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherNotificationList.clear();
                for (DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){

                    String SN_date = dataSnapshot2.child("date").getValue().toString();
                    String SN_message = dataSnapshot2.child("message").getValue().toString();
                    String SN_time = dataSnapshot2.child("time").getValue().toString();
                    String SN_senderEmail = dataSnapshot2.child("senderEmail").getValue().toString();
                    String SN_senderName = dataSnapshot2.child("senderEmail").getValue().toString();
                    String SN_notify = dataSnapshot2.child("notify").getValue().toString();
                    String SN_key = dataSnapshot2.getKey();

                    Log.e("112233","value: "+SN_date+SN_message+SN_time);

                    S_Notifications_AdminAdapter_Setter s_notifications_adminAdapter_setter = new S_Notifications_AdminAdapter_Setter(
                            SN_date,SN_message,SN_senderEmail,SN_senderName,SN_time,SN_notify,SN_key);

                    teacherNotificationList.add(s_notifications_adminAdapter_setter);

                    s_notifications_teacherAdapter.notifyDataSetChanged();
                }
                teacherNotificationCounter.setText(String.valueOf(teacherNotificationList.size()));
                alertDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    // FCMnotification
    private void updateToken(String token){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
//        Token token1 = new Token(token);
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        reference.child(firebaseUser.getUid()).setValue(token1);
    }


    public void NotificationDetail(String senderName,String senderEmail,String dateTime,String message,String type,String key){

        Button OkeyBtn;
        TextView DetailsenderName,DetailsenderEmail,DetailDateTime,DetailMessage;

        LayoutInflater factory = LayoutInflater.from(this);
        final View notificationView = factory.inflate(R.layout.activity_received_notifications_detail_design, null);


        OkeyBtn = notificationView.findViewById(R.id.OkeyBtn);
        DetailsenderName = notificationView.findViewById(R.id.DetailsenderName);
        DetailsenderEmail = notificationView.findViewById(R.id.DetailsenderEmail);
        DetailDateTime = notificationView.findViewById(R.id.DetailDateTime);
        DetailMessage = notificationView.findViewById(R.id.DetailMessage);

        AutofitHelper.create(DetailsenderName);
        AutofitHelper.create(DetailsenderEmail);
        AutofitHelper.create(DetailDateTime);

        DetailsenderName.setText(senderName);
        DetailsenderEmail.setText(senderEmail);
        DetailDateTime.setText(dateTime);
        DetailMessage.setText(message);

        final AlertDialog notificationDialog = new AlertDialog.Builder(this).create();
        notificationDialog.setView(notificationView);


        OkeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationDialog.cancel();
            }
        });

        final DatabaseReference dbr1 = FirebaseDatabase.getInstance().getReference()
                .child("Student")
                .child(myEmail.replace(".","_dot_"))
                .child("Notification")
                .child(type)
                .child(key);
        dbr1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("notify").getValue().toString().contains("true")){
                    dbr1.child("notify").setValue("false");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        notificationDialog.show();
    }
}
