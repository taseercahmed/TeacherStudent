package com.teacherstudent;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Feechallan extends AppCompatActivity {
    ConstraintLayout constraintLayoutdf;
    DatabaseReference mDatabaseReference;
    TextView textViewch;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    TextView feenotavailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feechallan);
        constraintLayoutdf=findViewById(R.id.ccffdd);
        textViewch=findViewById(R.id.textView4);
        Toolbar toolba=findViewById(R.id.toolbard);

        setSupportActionBar(toolba);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolba.setTitleTextColor(getResources().getColor(R.color.purewhite));
        getSupportActionBar().setTitle("Fee Challan");
      //  mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        toolba.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        feenotavailable=findViewById(R.id.textView41);

        readchallan();

    }

    String imageurl="";
    private void readchallan(){
        Bundle extras = getIntent().getExtras();
        final String className = extras.getString("className");
        final String sectionName = extras.getString("sectionName");
        String studygroup = extras.getString("studyGroup");

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                .child("Classes")
                .child(className)
                .child(sectionName)
                .child("StudyGroup")
                .child(studygroup);
        databaseReference1.child("feechaalan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("challan").exists()){
                        imageurl=dataSnapshot.child("challan").getValue().toString();
                        textViewch.setText("This Challan form for class "+className+" section  "+sectionName+".Submit your Dues before Deadline.");
                        constraintLayoutdf.setVisibility(View.VISIBLE);
                      //  chaalantext.setText("Challan Form.pdf");
                        feenotavailable.setVisibility(View.GONE);
                        readchal(imageurl);

                    }
                    else{
                        constraintLayoutdf.setVisibility(View.GONE);
                        feenotavailable.setVisibility(View.VISIBLE);
                        //    Toast.makeText(FeesActivity.this, "not exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    constraintLayoutdf.setVisibility(View.GONE);
                    feenotavailable.setVisibility(View.VISIBLE);
                    // Toast.makeText(FeesActivity.this, "node not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void readchal(final String imageStorageLink) {
        constraintLayoutdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(imageStorageLink)){
                    Toast.makeText(Feechallan.this, "Downloading", Toast.LENGTH_SHORT).show();
                    downloadFile(Feechallan.this,"Challan form","pdf",DIRECTORY_DOWNLOADS,imageStorageLink);
                }
            }
        });
    }
    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);

    }


}