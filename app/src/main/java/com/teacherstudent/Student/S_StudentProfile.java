package com.teacherstudent.Student;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teacherstudent.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class S_StudentProfile extends AppCompatActivity {

    TextInputEditText SP_name,SP_email,SP_age,SP_phNo,SP_bloodGroup;
    Button SP_nextBtn,SP_cancelBtn;
    CircleImageView SP_image;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    public AlertDialog alertDialog;

    Spinner spin;
    String spin_val;
    String[] gender = { "Male", "Female" };

    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap;
    UploadTask uploadTask;
    Uri profileImageUri;
    static String imageStorageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_student_profile);

        SP_name = findViewById(R.id.SP_name);
        SP_email = findViewById(R.id.SP_email);
        SP_age = findViewById(R.id.SP_age);
        SP_phNo = findViewById(R.id.SP_phNo);
        SP_bloodGroup = findViewById(R.id.SP_bloodGroup);
        SP_nextBtn = findViewById(R.id.SP_nextBtn);
        SP_cancelBtn = findViewById(R.id.SP_cancelBtn);
        spin = (Spinner) findViewById(R.id.spinner2);
        SP_image = findViewById(R.id.SP_image);

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(S_StudentProfile.this, android.R.layout.simple_spinner_item, gender);
        spin_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spin.setAdapter(spin_adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                spin_val = gender[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });





        alertDialog = new SpotsDialog.Builder()
                .setContext(S_StudentProfile.this)
                .setMessage("Loading")
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
               // if (dataSnapshot.child("name").exists()&&dataSnapshot.child("age").exists()&&)
                SP_name.setText(dataSnapshot.child("name").getValue().toString());
                SP_email.setText(myEmail);
                SP_age.setText(dataSnapshot.child("age").getValue().toString());
                SP_phNo.setText(myEmail.replace(".","_dot_"));
                if (dataSnapshot.child("bloodGroup").exists()){
                    SP_bloodGroup.setText(dataSnapshot.child("bloodGroup").getValue().toString());
                }
                if (dataSnapshot.child("gender").exists()){
                    spin_val = dataSnapshot.child("gender").getValue().toString();
                    if (!spin_val.equals("Male")){
                        spin.setSelection(1);
                    }else {
                        spin.setSelection(0);
                    }
                }
                if (dataSnapshot.child("profileImageLink").exists()){
                    Glide.with(getApplicationContext())
                            .load(dataSnapshot.child("profileImageLink").getValue().toString())
                            .into(SP_image);
                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        SP_nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
                if (profileImageUri!=null) {
                    imageStorageLink = uploadToStorage(profileImageUri);
                }else {
                    uploadToRealTime();
                }
            }
        });




        SP_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        SP_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(S_StudentProfile.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
//                cropImageView.setVisibility(View.GONE);
                SP_image.setVisibility(View.VISIBLE);
                profileImageUri = result.getUri();
                Glide.with(S_StudentProfile.this)
                        .load(profileImageUri)
                        .into(SP_image);
                /*uri=resultUri;
                profileImageUri=resultUri.toString();*/


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



    public String uploadToStorage(Uri filePath){

        Uri uri = filePath;

        StorageReference thumb_filePath = storageReference.child("ProfileImage/" + myEmail + "/" + "image.jpg");
        if (bitmap == null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //Log.e("ruller",bitmap.toString());
                //Toast.makeText(this, "it was empty", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e("112233", e.getMessage());
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        final byte[] thumb_byte = byteArrayOutputStream.toByteArray();
        uploadTask = thumb_filePath.putBytes(thumb_byte);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downladurl=urlTask.getResult();
                imageStorageLink =downladurl.toString();
                Log.d("112233",imageStorageLink);
                uploadToRealTime();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(S_StudentProfile.this, "Uploading failed..", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        return imageStorageLink;
    }

    public void uploadToRealTime(){
        String name = SP_name.getText().toString();
        String age = SP_age.getText().toString();
        String phoneNo = SP_phNo.getText().toString();
        String bloodGroup = SP_bloodGroup.getText().toString();

        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Student")
                .child(myEmail.replace(".","_dot_"))
                .child("Profile");

        Map port = new HashMap();
        port.put("age",age);
        port.put("name",name);
        port.put("phoneNumber",phoneNo);
        port.put("bloodGroup",bloodGroup);
        port.put("gender",spin_val);
        port.put("profileImageLink",imageStorageLink);

        dbr.setValue(port).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                alertDialog.cancel();
                Toast.makeText(S_StudentProfile.this, "Updated.", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.cancel();
                Toast.makeText(S_StudentProfile.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
