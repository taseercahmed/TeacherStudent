package com.teacherstudent;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class WriteDiary extends AppCompatActivity {

    public AlertDialog alertDialog;
    String className,sectionName,studyGroup,subjectName;

    EditText writeDiaryDate;
    EditText writeDiaryDetail;
    Button writeDiaryUploadBtn,writeDiaryCancelBtn;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);


        writeDiaryDate = findViewById(R.id.writeDiaryDate);
        writeDiaryDetail = findViewById(R.id.writeDiaryDetail);
        writeDiaryUploadBtn = findViewById(R.id.writeDiaryUploadBtn);
        writeDiaryCancelBtn = findViewById(R.id.writeDiaryCancelBtn);


        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();


        Bundle extras1 = getIntent().getExtras();
        className = extras1.getString("className");
        sectionName = extras1.getString("sectionName");
        studyGroup = extras1.getString("studyGroup");
        subjectName = extras1.getString("subjectName");

        //Log.e("112233",className+sectionName+studyGroup+subjectName);

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = df1.format(Calendar.getInstance().getTime());
        writeDiaryDate.setText(currentDate);




        writeDiaryUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {

                    DateFormat df2 = new SimpleDateFormat("h:mm a");
                    final String currentTime = df2.format(Calendar.getInstance().getTime());

                    Map port = new HashMap();
                    port.put("diaryDetail", writeDiaryDetail.getText().toString());
                    port.put("diaryTime", currentTime);
                    port.put("diarySenderEmail", myEmail);

                    final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                            .child("Classes")
                            .child(className)
                            .child(sectionName)
                            .child("StudyGroup")
                            .child(studyGroup)
                            .child("Subjects")
                            .child(subjectName)
                            .child("Diaries");
                    dbr.child(currentDate).setValue(port).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Map port2 = new HashMap();
                            port2.put("message", writeDiaryDetail.getText().toString());
                            port2.put("time", currentTime);
                            port2.put("date", currentDate);
                            final DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference()
                                    .child("Teacher")
                                    .child(myEmail.replace(".","_dot_"))
                                    .child("Recents")
                                    .child("Diary");
                            dbr2.push().setValue(port2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendNotificationToUser(writeDiaryDetail.getText().toString());
                                    Toast.makeText(WriteDiary.this, "Diary Sent", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //e.printStackTrace();
                                    Toast.makeText(WriteDiary.this, ""+e, Toast.LENGTH_SHORT).show();
                                    //Log.e("112233", "onFailure: "+e);
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //e.printStackTrace();
                            Toast.makeText(WriteDiary.this, ""+e, Toast.LENGTH_SHORT).show();
                            //Log.e("112233", "onFailure: "+e);

                        }
                    });



                }
            }
        });

        writeDiaryCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendNotificationToUser(final String msg) {




        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tokens");
        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Classes")
                .child(className)
                .child(sectionName)
                .child("StudyGroup")
                .child(studyGroup)
                .child("Students");
        dbr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String studentemail=dataSnapshot1.getKey();

                            databaseReference.orderByKey().equalTo(studentemail).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                        String token=dataSnapshot1.child("token").getValue().toString();
                                        RootModel rootModel = new RootModel(token, new NotificationModel("New Message",msg), new DataModel("message", msg));

                                        ApiInterface apiService =  ApiClient.getClient().create(ApiInterface.class);
                                        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

                                        responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                                Log.d("abc","Successfully notification send by using retrofit.");
                                                Toast.makeText(WriteDiary.this,"Successfully Sent to user",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });










    }

    private boolean check(){
        boolean result=true;
        final String diary=writeDiaryDetail.getText().toString();

        if(TextUtils.isEmpty(diary)){
            writeDiaryDetail.setError("Write diary.");
            result=false;
        }

        return result;
    }
}
