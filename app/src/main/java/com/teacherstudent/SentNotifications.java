package com.teacherstudent;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import me.grantland.widget.AutofitHelper;

public class SentNotifications extends AppCompatActivity {

    ArrayList<SentNotifications_Setter> sentNotificationsList = new ArrayList<>();
    SentNotifications_Adapter sentNotifications_adapter;
    RecyclerView recyclerView;
    public AlertDialog alertDialog;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    FloatingActionButton openWriteNotification;
    String className,sectionName,studyGroup,subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_notifications);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sent Notifications");

        openWriteNotification = findViewById(R.id.send_notification_floating_btn);

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();


        sentNotifications_adapter = new SentNotifications_Adapter(this, sentNotificationsList);// constructor
        recyclerView = findViewById(R.id.sent_notifications_recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(sentNotifications_adapter);

        Bundle extras = getIntent().getExtras();
        className = extras.getString("className");
        sectionName = extras.getString("sectionName");
        studyGroup = extras.getString("studyGroup");
        subjectName = extras.getString("subjectName");

        alertDialog = new SpotsDialog.Builder()
                .setContext(SentNotifications.this)
                .setMessage("Loading")
                .setCancelable(false)
                .build();
        alertDialog.show();



        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Teacher")
                .child(myEmail.replace(".","_dot_"))
                .child("TimeTable")
                .child(className)
                .child(sectionName)
                .child(studyGroup)
                .child(subjectName)
                .child("SentNotifications");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sentNotificationsList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    final String key1 = dataSnapshot1.getKey();

                    String SN_date = dataSnapshot.child(key1).child("date").getValue().toString();
                    String SN_messae = dataSnapshot.child(key1).child("message").getValue().toString();
                    String SN_receiverEmails = dataSnapshot.child(key1).child("receiverEmails").getValue().toString();
                    String SN_sendedTo = dataSnapshot.child(key1).child("sendedTo").getValue().toString();
                    String SN_senderEmail = dataSnapshot.child(key1).child("senderEmail").getValue().toString();
                    String SN_senderName = dataSnapshot.child(key1).child("senderName").getValue().toString();
                    String SN_subject = dataSnapshot.child(key1).child("subject").getValue().toString();
                    String SN_time = dataSnapshot.child(key1).child("time").getValue().toString();

                    SentNotifications_Setter sentNotifications_setter = new SentNotifications_Setter(
                            SN_date,
                            SN_messae,
                            SN_receiverEmails,
                            SN_sendedTo,
                            SN_senderEmail,
                            SN_senderName,
                            SN_subject,
                            SN_time);
                    sentNotificationsList.add(sentNotifications_setter);
                    sentNotifications_adapter.notifyDataSetChanged();
                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        openWriteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SentNotifications.this, WriteNotification.class);
                intent.putExtra("className", className);
                intent.putExtra("sectionName", sectionName);
                intent.putExtra("studyGroup", studyGroup);
                intent.putExtra("subjectName", subjectName);
                startActivity(intent);
            }
        });
    }

    public void SN_detailDialogBox(String sendedTo,String receiverEmails,String date,String time,String subject,String message){
        Button OkeyBtn;
        TextView SN_sendedTo,SN_receiverEmails,SN_date,SN_time,SN_subject,SN_message;

        LayoutInflater factory = LayoutInflater.from(this);
        final View notificationView = factory.inflate(R.layout.activity_sent_notifications_detail, null);


        OkeyBtn = notificationView.findViewById(R.id.SN_okeyBtn);
        SN_sendedTo = notificationView.findViewById(R.id.SN_sendedTo);
        SN_receiverEmails = notificationView.findViewById(R.id.SN_receiverEmails);
        SN_date = notificationView.findViewById(R.id.SN_date);
        SN_time = notificationView.findViewById(R.id.SN_time);
        SN_subject = notificationView.findViewById(R.id.SN_subject);
        SN_message = notificationView.findViewById(R.id.SN_message);

//        AutofitHelper.create(DetailsenderName);
//        AutofitHelper.create(DetailsenderEmail);
//        AutofitHelper.create(DetailDateTime);

        AutofitHelper.create(SN_receiverEmails);
        SN_receiverEmails.setMovementMethod(new ScrollingMovementMethod());

        SN_sendedTo.setText(sendedTo);
        SN_receiverEmails.setText(receiverEmails.substring(1, receiverEmails.length()-1).replace(", ","\n").replace("_dot_","."));
        SN_date.setText(date);
        SN_time.setText(time);
        SN_subject.setText(subject);
        SN_message.setText(message);


        final AlertDialog notificationDialog = new AlertDialog.Builder(this).create();
        notificationDialog.setView(notificationView);


        OkeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationDialog.cancel();
            }
        });

        notificationDialog.show();
    }

//    private void registerToken() {
//        // if(AppProfileInfo.getAppType().equals("Parent")){
//        // txtAppType.setText(AppProfileInfo.getAppType());
//        // }
//        OkHttpClient client = new OkHttpClient();
//        MyFirebaseInstanceService firebaseInstanceService=new MyFirebaseInstanceService();
//        RequestBody body = new FormBody.Builder()
//                .add("token",firebaseInstanceService.returnTokenf())
//                .add("email",FirebaseAuth.getInstance().getCurrentUser().getEmail())
//                .add("type",readUserType())
//                .add("date",timeS())
//
//                .build();
//        Log.d("url","https://bvoir.website/serverFolder/register.php");
//        Request request = new Request.Builder()
//                .url("https://bvoir.website/serverFolder/register.php")
//                .post(body)
//                .build();
//
//        try {
//            int SDK_INT = Build.VERSION.SDK_INT;
//            if (SDK_INT > 8) {
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                        .permitAll().build();
//                StrictMode.setThreadPolicy(policy);
////your codes here
//                client.newCall(request).execute();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
