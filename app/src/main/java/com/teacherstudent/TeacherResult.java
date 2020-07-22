package com.teacherstudent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class TeacherResult extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String className,sectionName,studyGroup,subjectName;
    String currentDate,currentTime;
    SpotsDialog alertDialog;
    ArrayList<ResultClass> list=new ArrayList<>();
    List<String> list1=new ArrayList<>();
    TextInputEditText teachername,subject,grade,marks,totalmarks;
    EditText selectsudent;
    Spinner spinnerl;
    String useremail="";

//    private void setupSpinner() {
//
//        //@SuppressLint("ResourceType") ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, android.R.layout.simple_spinner_item, list1);
//
//
//        spinnerl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selection = (String) parent.getItemAtPosition(position);
//                if (!TextUtils.isEmpty(selection)) {
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_result);
        teachername=findViewById(R.id.teachername);
        subject=findViewById(R.id.subjectname);
        grade=findViewById(R.id.grade);
        marks=findViewById(R.id.marks);
        totalmarks=findViewById(R.id.totalmarks);
        spinnerl=findViewById(R.id.spinner_gender);
        spinnerl.setOnItemSelectedListener(this);
        Toolbar toolba=findViewById(R.id.toolbard);

        setSupportActionBar(toolba);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolba.setTitleTextColor(getResources().getColor(R.color.purewhite));
        getSupportActionBar().setTitle("Upload Result");

        toolba.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        className = extras.getString("className");
        sectionName = extras.getString("sectionName");
        studyGroup = extras.getString("studyGroup");
        subjectName = extras.getString("subjectName");
        subject.setText(subjectName);
        readteacher();
        alertDialog = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(TeacherResult.this)
                .setMessage("loading")
                .setCancelable(false)
                .build();

        reademsil();



    }




    private void readteacher() {
        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();
        dbr.child("Teacher").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","_dot_")).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.child("name").exists()){
                teachername.setText(dataSnapshot.child("name").getValue().toString()+"");

            }else{
                Toast.makeText(TeacherResult.this, "xjkxk", Toast.LENGTH_SHORT).show();
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void reademsil() {
     //   Toast.makeText(this, "nhfkjhjfd", Toast.LENGTH_SHORT).show();
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
                 list1.clear();
                 list.clear();
                 list1.add("Select");
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    String email=dataSnapshot1.getKey();
                    String name=dataSnapshot1.child("name").getValue().toString();

                    ResultClass resultClass=new ResultClass(name,email);
                    if (list.contains(email)){

                    }else{
                        list.add(resultClass);
                    }
                    if (list1.contains(name)){

                    }else{
                        list1.add(name);
                    }

                    Log.e("112233resut","jkhj"+dataSnapshot1.child("name").getValue().toString());



                }
                ArrayAdapter<String> genderSpinnerAdapter = new ArrayAdapter<String>(TeacherResult.this, android.R.layout.simple_spinner_item, list1);
                genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                spinnerl.setAdapter(genderSpinnerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                                RootModel rootModel = new RootModel(token, new NotificationModel("Result",msg), new DataModel("message", msg));

                                ApiInterface apiService =  ApiClient.getClient().create(ApiInterface.class);
                                retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

                                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                                        Log.d("abc","Successfully notification send by using retrofit.");

                                        Toast.makeText(TeacherResult.this,"Successfully Sent to user",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        for (ResultClass resultClass:list){
            if (item.equals(resultClass.getName())){
                useremail=resultClass.getEmail();
             //   Toast.makeText(this, "dff"+useremail, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void clicksend(View view) {

      if (check()){
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
                  .child(useremail.replace(".","_dot_"));
          String key=dbr.push().getKey();



          final Map port3 = new HashMap();

          port3.put("time", currentTime);
          port3.put("date", currentDate);
          port3.put("subjectname",subjectName);
          port3.put("teachername",teachername.getText().toString());
          port3.put("grade",grade.getText().toString());
          port3.put("marks",marks.getText().toString());
          port3.put("studentname",useremail);
          port3.put("totalmarks",totalmarks.getText().toString());


          dbr.child(subjectName).setValue(port3).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                  final DatabaseReference dbr4 = FirebaseDatabase.getInstance().getReference()
                          .child("Teacher")
                          .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","_dot_"))
                  .child("Result")
                          .child(subjectName);
                  dbr4.child(useremail.replace(".","_dot_")).setValue(port3).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          Toast.makeText(TeacherResult.this, "Result Sent Sucesssfully", Toast.LENGTH_SHORT).show();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          //e.printStackTrace();
                          Toast.makeText(TeacherResult.this, ""+e, Toast.LENGTH_SHORT).show();
                          //Log.e("112233", "onFailure: "+e);
                      }
                  });
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

              }
          });



      }

    }

    private boolean check() {
        boolean isFilled=true;

        String g = grade.getText().toString();
        String mark = marks.getText().toString();
        String tmark = totalmarks.getText().toString();

        if(TextUtils.isEmpty(tmark)){
            isFilled=false;
            totalmarks.setError("Fill this");
        }
        if(TextUtils.isEmpty(g)){
            isFilled=false;
            grade.setError("Fill this");
        }
        if(TextUtils.isEmpty(mark)){
            isFilled=false;
            marks.setError("Fill this");
        }
        return isFilled;
    }
}