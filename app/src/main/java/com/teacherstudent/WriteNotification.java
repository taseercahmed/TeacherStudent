package com.teacherstudent;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teacherstudent.Service.ApiClient;
import com.teacherstudent.Service.ApiInterface;
import com.teacherstudent.Service.DataModel;
import com.teacherstudent.Service.NotificationModel;
import com.teacherstudent.Service.RootModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.teacherstudent.WriteNotification_Adapter.selectedStudentList;

public class WriteNotification extends AppCompatActivity {

    ArrayList<WriteNotification_Setter> studentsEmailList = new ArrayList<>();
    WriteNotification_Adapter writeNotification_adapter;
    public AlertDialog alertDialog;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    String className,sectionName,studyGroup,subjectName;
    RecyclerView recyclerView;

    static CheckBox selectAll;
    ImageView WN_sendNotification;
    TextInputEditText WN_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_notification);

        selectAll = findViewById(R.id.selectAll);
        WN_sendNotification = findViewById(R.id.WN_sendNotification);
        WN_message = findViewById(R.id.WN_message);

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        writeNotification_adapter = new WriteNotification_Adapter(this, studentsEmailList);// constructor
        recyclerView = findViewById(R.id.WN_recyclerView);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(writeNotification_adapter);

        Bundle extras = getIntent().getExtras();
        className = extras.getString("className");
        sectionName = extras.getString("sectionName");
        studyGroup = extras.getString("studyGroup");
        subjectName = extras.getString("subjectName");

        alertDialog = new SpotsDialog.Builder()
                .setContext(WriteNotification.this)
                .setMessage("Sending")
                .setCancelable(false)
                .build();
        alertDialog.show();


        final DatabaseReference dbr1 = FirebaseDatabase.getInstance().getReference()
                .child("Classes")
                .child(className)
                .child(sectionName)
                .child("StudyGroup")
                .child(studyGroup)
                .child("Students");
        dbr1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                studentsEmailList.clear();
                selectedStudentList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String WN_studentsEmail = dataSnapshot1.getKey();

                    //Log.e("112233", "emails: " +WN_studentsEmail);

                    WriteNotification_Setter writeNotification_setter = new WriteNotification_Setter(WN_studentsEmail);
                    studentsEmailList.add(writeNotification_setter);
                    writeNotification_adapter.notifyDataSetChanged();

                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // select all
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectAll.isChecked()){
                    writeNotification_adapter.selectAll();
                }
                if (!selectAll.isChecked()){
                    writeNotification_adapter.unselectall();
                }
                writeNotification_adapter.notifyDataSetChanged();
            }
        });


        WN_sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (check()) {

                    alertDialog.show();

                    DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat df2 = new SimpleDateFormat("h:mm a");


                    final String message = WN_message.getText().toString();
                    final String currentDate = df1.format(Calendar.getInstance().getTime());
                    final String currentTime = df2.format(Calendar.getInstance().getTime());




                    final Map port = new HashMap();
                    port.put("date", currentDate);
                    port.put("message", message);
                    port.put("senderEmail", myEmail);
                    port.put("subject", subjectName);
                    port.put("time", currentTime);
                    port.put("notify", "true");
                    for (int i = 0; i < selectedStudentList.size(); i++) {
                        Log.e("112233", "all list" + selectedStudentList.get(i));
                        final String studentEmail = selectedStudentList.get(i).toString();

                        //sendNotificationToPhp(message,currentTime,currentDate,studentEmail);


                        final DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference()
                                .child("Student").child(studentEmail).child("Notification").child("Teacher");
                        dbr2.push().setValue(port).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("112233", "Sent notification: " + studentEmail + myEmail + message);

                                WN_message.setText(null);
                                sendNotificationToUser(studentEmail,message);
                                // hide softkeyboard
                                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
                            }
                        });

                    }

                    alertDialog.dismiss();

                    Toast.makeText(WriteNotification.this, "Notification sent.", Toast.LENGTH_SHORT).show();





                    // for uploading in teacher side
                    String sendedTo="";
                    if (selectedStudentList.size()==studentsEmailList.size()){
                        sendedTo="All students";
                    }else {
                        sendedTo="Specific students";
                    }
                    final Map port2 = new HashMap();
                    port2.put("date", currentDate);
                    port2.put("message", message);
                    port2.put("receiverEmails",selectedStudentList.toString());
                    port2.put("sendedTo",sendedTo);
                    port2.put("senderEmail", myEmail);
                    port2.put("subject", subjectName);
                    port2.put("senderName"," ");
                    port2.put("time", currentTime);
                    final DatabaseReference dbr3 = FirebaseDatabase.getInstance().getReference()
                            .child("Teacher")
                            .child(myEmail.replace(".","_dot_"))
                            .child("TimeTable")
                            .child(className)
                            .child(sectionName)
                            .child(studyGroup)
                            .child(subjectName)
                            .child("SentNotifications");
                    final String finalSendedTo = sendedTo;
                    dbr3.push().setValue(port2).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e("112233", "Sent notification to teacher side: " + selectedStudentList + finalSendedTo + myEmail + message);
                        }
                    });





                    Map port3 = new HashMap();
                    port3.put("message", message);
                    port3.put("time", currentTime);
                    port3.put("date", currentDate);
                    final DatabaseReference dbr4 = FirebaseDatabase.getInstance().getReference()
                            .child("Teacher")
                            .child(myEmail.replace(".","_dot_"))
                            .child("Recents")
                            .child("SentNotification");
                    dbr4.push().setValue(port3).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Toast.makeText(WriteNotification.this, "Diary Sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //e.printStackTrace();
                            Toast.makeText(WriteNotification.this, ""+e, Toast.LENGTH_SHORT).show();
                            //Log.e("112233", "onFailure: "+e);
                        }
                    });




                    //fcmNotification
                    //sendNotification("faizanyousaf09@gmail_dot_com","Hamza","hey congratulation");
                //    *//*final String msg = message;

                    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          //  User user = dataSnapshot.getValue(User.class);
                         //   sendNotification("faizanyousaf09@gmail_dot_com","Hamza","hey congratulation");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }
        });

    }
/*
//    *//*//*/ //fcmNotification
//    private void sendNotification(String receiver, final String username, final String message){
//        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
//        Query query = tokens.orderByKey().equalTo(receiver);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
//                    Token token = snapshot.getValue(Token.class);
//
//                    Log.e("112233","sented"+token);
//
//
//                    Data data = new Data("faizanyousaf09@gmail_dot_com",R.mipmap.ic_launcher, username+": "+message,"New message","sented");
//
//                    Sender sender = new Sender(data,token.getToken());
//
//                    apiService.sendNotification(sender)
//                            .enqueue(new Callback<MyResponse>() {
//                                @Override
//                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                    Log.e("112233","body"+response.body().toString());
//
//                                    if (response.code() == 200){
//                                        if (response.body().success != 1){
//                                            Toast.makeText(WriteNotification.this, "Failed!", Toast.LENGTH_SHORT).show();
//                                        }else {
//                                            Toast.makeText(WriteNotification.this, "success", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MyResponse> call, Throwable t) {
//                                    Toast.makeText(WriteNotification.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    private boolean check(){
        boolean result=true;
        final String message=WN_message.getText().toString().trim();

        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "Notification cannot be empty.", Toast.LENGTH_SHORT).show();
            //WN_message.setError("Email is empty");
            result=false;
        }
        return result;
    }

    private void sendNotificationToUser(String studentemail, final String msg) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tokens");
        databaseReference.orderByKey().equalTo(studentemail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String token=dataSnapshot1.child("token").getValue().toString();
                    // String token="drfmwyOKHAY:APA91bHCKHuk-OL8uGorLDGBkyXhBW23o5Fsduj3q7Q62AfOPFel9ybW7-V5yWgaB7KnvjbVWiOqgFuH1fnzjjNQCz9siQYAX9wNZEcF0vkX-HDHWw6fhY7pOvLEl_YqUrHIluRz8GgK";

                    RootModel rootModel = new RootModel(token, new NotificationModel("New Message",msg), new DataModel("message", msg));

                    ApiInterface apiService =  ApiClient.getClient().create(ApiInterface.class);
                    Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("abc","Successfully notification send by using retrofit.");
                            Toast.makeText(WriteNotification.this,"Sent Successfully",Toast.LENGTH_SHORT).show();
                            WN_message.setText("");
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
