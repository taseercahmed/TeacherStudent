package com.teacherstudent;

import android.app.AlertDialog;
import android.os.Bundle;
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

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import me.grantland.widget.AutofitHelper;

public class ReceivedNotifications extends AppCompatActivity {

    ArrayList<ReceivedNotifications_Setter> notificationsList = new ArrayList<>();
    ReceivedNotifications_Adapter receivedNotifications_adapter;
    public AlertDialog alertDialog;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_notifications);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");



        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();


        receivedNotifications_adapter = new ReceivedNotifications_Adapter(this, notificationsList);// constructor
        recyclerView = findViewById(R.id.received_notifications_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(receivedNotifications_adapter);


        alertDialog = new SpotsDialog.Builder()
                .setContext(ReceivedNotifications.this)
                .setMessage("Loading")
                .setCancelable(false)
                .build();
        alertDialog.show();


            final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                    .child("Teacher")
                    .child(myEmail.replace(".","_dot_"))
                    .child("Notification")
                    .child("Admin");
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notificationsList.clear();
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        String notificationDate = dataSnapshot1.child("date").getValue().toString();
                        String notificationTime = dataSnapshot1.child("time").getValue().toString();
                        String notificationMsg = dataSnapshot1.child("message").getValue().toString();
                        String senderName = dataSnapshot1.child("senderEmail").getValue().toString();
                        String senderEmail = dataSnapshot1.child("senderEmail").getValue().toString();

                        String RN_notify = dataSnapshot1.child("notify").getValue().toString();
                        String RN_key = dataSnapshot1.getKey();

                        ReceivedNotifications_Setter receivedNotifications_setter = new ReceivedNotifications_Setter(
                                notificationDate,
                                notificationTime,
                                notificationMsg,
                                senderName,
                                senderEmail,
                                RN_notify,
                                RN_key);

                        notificationsList.add(receivedNotifications_setter);
                    }
                    receivedNotifications_adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }

    public void NotificationDetail(String senderName,String senderEmail,String dateTime,String message,String key){

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

        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Teacher")
                .child(myEmail.replace(".","_dot_"))
                .child("Notification")
                .child("Admin")
                .child(key);
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("notify").getValue().toString().contains("true")){
                    dbr.child("notify").setValue("false");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        notificationDialog.show();
    }
}
