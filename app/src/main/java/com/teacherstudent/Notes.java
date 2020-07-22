package com.teacherstudent;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.teacherstudent.Service.ApiClient;
import com.teacherstudent.Service.ApiInterface;
import com.teacherstudent.Service.DataModel;
import com.teacherstudent.Service.NotificationModel;
import com.teacherstudent.Service.RootModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import okhttp3.ResponseBody;
import retrofit2.Callback;

public class Notes extends AppCompatActivity {

    ArrayList<Notes_Setter> notesList = new ArrayList<>();
    Notes_Adapter notes_adapter;
    RecyclerView recyclerView;

    ImageView N_attachmentBtn;
    ConstraintLayout N_includedAttachment;
    TextView N_takeImage,N_gallery,N_document;

    public AlertDialog alertDialog;

    private static final int CAPTURE_MEDIA = 368;
    private static final int SELECT_IMAGE = 7;
    private static final int PICKFILE_REQUEST_CODE = 10;
    FirebaseStorage storage;
    StorageReference storageReference;
    Bitmap bitmap;
    UploadTask uploadTask;

    String myEmail;
    FirebaseAuth mAuth;
    FirebaseUser fu;

    String className,sectionName,studyGroup,subjectName;
    String currentDate,currentTime;
    Button N_sendBtn;
    EditText N_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notes");

        N_attachmentBtn = findViewById(R.id.N_attachmentBtn);
        N_includedAttachment = findViewById(R.id.N_includedAttachment);
        N_takeImage = findViewById(R.id.N_takeImage);
        N_gallery = findViewById(R.id.N_gallery);
        N_document = findViewById(R.id.N_document);
        N_sendBtn = findViewById(R.id.N_sendBtn);
        N_message = findViewById(R.id.N_message);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        fu = mAuth.getCurrentUser();
        myEmail = fu.getEmail();

        notes_adapter = new Notes_Adapter(this, notesList);// constructor
        recyclerView = findViewById(R.id.notesListRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notes_adapter);

        Bundle extras = getIntent().getExtras();
        className = extras.getString("className");
        sectionName = extras.getString("sectionName");
        studyGroup = extras.getString("studyGroup");
        subjectName = extras.getString("subjectName");

        alertDialog = new SpotsDialog.Builder()
                .setContext(Notes.this)
                .setMessage("Uploading")
                .setCancelable(false)
                .build();

        Log.e("112233", "extraValue:" + className + sectionName + studyGroup + subjectName);




        final DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference()
                .child("Teacher")
                .child(myEmail.replace(".","_dot_"))
                .child("TimeTable")
                .child(className)
                .child(sectionName)
                .child(studyGroup)
                .child(subjectName)
                .child("Notes");
        dbr2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notesList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final String notesKey = dataSnapshot1.getKey();

                    String documentDate = dataSnapshot1.child("documentDate").getValue().toString();
                    String documentMessage = dataSnapshot1.child("documentMessage").getValue().toString();
                    String documentSenderEmail = dataSnapshot1.child("documentSenderEmail").getValue().toString();
                    String documentTime = dataSnapshot1.child("documentTime").getValue().toString();
                    String documentType = dataSnapshot1.child("documentType").getValue().toString();
                    String subjectName = dataSnapshot1.child("subjectName").getValue().toString();

                    Log.e("112233", "diary: " + documentDate + documentMessage + documentSenderEmail + documentTime + documentType);

                    Notes_Setter notes_setter = new Notes_Setter(documentDate,documentMessage,documentSenderEmail,documentTime,documentType,subjectName);
                    notesList.add(notes_setter);
                    notes_adapter.notifyDataSetChanged();

                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });










        N_attachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (N_includedAttachment.getVisibility()==View.GONE){
                    N_includedAttachment.setVisibility(View.VISIBLE);
                }else {
                    N_includedAttachment.setVisibility(View.GONE);
                }
            }
        });


        N_takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Notes.this);
            }
        });

        N_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/* video/*");
                startActivityForResult(pickIntent, SELECT_IMAGE);
            }
        });

        N_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] mimeTypes =
                        {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                "text/plain",
                                "application/pdf",
                                "application/zip"};

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    if (mimeTypes.length > 0) {
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }
                } else {
                    String mimeTypesStr = "";
                    for (String mimeType : mimeTypes) {
                        mimeTypesStr += mimeType + "|";
                    }
                    intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
                }
                startActivityForResult(Intent.createChooser(intent,"ChooseFile"), PICKFILE_REQUEST_CODE);
            }
        });



        N_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = N_message.getText().toString();
                if(TextUtils.isEmpty(message)){
                    N_message.setError("Empty");
                }else {
                    sendNotificationToUser(message);
                    uploadToDatabse(message, "message");
                    N_message.setText(null);
                }
            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        // gallery
        if (resultCode == RESULT_OK && requestCode==SELECT_IMAGE) {
            Uri selectedMediaUri = data.getData();
            if (selectedMediaUri.toString().contains("image")) {
                String type = getMimeType(this,selectedMediaUri);
                alertDialog.show();
                Log.e("112233",""+selectedMediaUri);
                uploadToStorage(selectedMediaUri,type);
            } else if (selectedMediaUri.toString().contains("video")) {
                String type = getMimeType(this,selectedMediaUri);
                alertDialog.show();
                Log.e("112233",""+selectedMediaUri);
                uploadToStorage(selectedMediaUri,type);
            }
        }




        // image capture
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.e("112233","imageUri: "+resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }




        // document
        if (requestCode == PICKFILE_REQUEST_CODE  && resultCode == RESULT_OK && data != null && data.getData() != null){
            alertDialog.show();
            Uri filePath = data.getData();
            Log.e("112233",filePath.toString());
            String type = getMimeType(this,filePath);
            Log.e("112233","type: "+type);
            uploadToStorage(filePath,type);
        }

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
                                        Toast.makeText(Notes.this,"Successfully Sent to user",Toast.LENGTH_SHORT).show();
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


    public void uploadToStorage(Uri filePath, final String type){

        Uri uri = filePath;
        StorageReference thumb_filePath = storageReference.child("Notes/" + myEmail + "/" + subjectName + "/" + UUID.randomUUID().toString() + "." + type);


        if (type.contains("jpg") || type.contains("jpeg") || type.contains("png")) {
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
        }else {
            uploadTask = thumb_filePath.putFile(uri);
        }


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downladurl=urlTask.getResult();
                String imagepath=downladurl.toString();
                Log.d("112233",imagepath);

                uploadToDatabse(imagepath,type);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Notes.this, "Uploading failed..", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    public void uploadToDatabse(String message,String type){

        DateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = df1.format(Calendar.getInstance().getTime());
        DateFormat df2 = new SimpleDateFormat("h:mm a");
        currentTime = df2.format(Calendar.getInstance().getTime());




        Map port=new HashMap();
        port.put("documentDate", currentDate);
        port.put("documentMessage", message);
        port.put("documentSenderEmail", myEmail);
        port.put("documentTime",currentTime);
        port.put("documentType", type);
        port.put("subjectName", subjectName);
        final DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                .child("Classes")
                .child(className)
                .child(sectionName)
                .child("StudyGroup")
                .child(studyGroup)
                .child("Subjects")
                .child(subjectName)
                .child("Notes");
        dbr.push().setValue(port).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(Notes.this, "Sent", Toast.LENGTH_LONG).show();
            }
        });




        final DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference()
                .child("Teacher")
                .child(myEmail.replace(".","_dot_"))
                .child("TimeTable")
                .child(className)
                .child(sectionName)
                .child(studyGroup)
                .child(subjectName)
                .child("Notes");
        dbr2.push().setValue(port).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //msg.setText("");
                Toast.makeText(Notes.this, "Uploaded", Toast.LENGTH_SHORT).show();
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
                .child("Notes");
        dbr4.push().setValue(port3).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(WriteNotification.this, "Diary Sent", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //e.printStackTrace();
                Toast.makeText(Notes.this, ""+e, Toast.LENGTH_SHORT).show();
                //Log.e("112233", "onFailure: "+e);
            }
        });
        alertDialog.dismiss();
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    private void downloadFile() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("<your_bucket>");
        StorageReference islandRef = storageRef.child("file.txt");

        File rootPath = new File(Environment.getExternalStorageDirectory(), "file_name");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,"imageName.txt");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });
    }


        /*    Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent newIntent = Intent.createChooser(intent, "Open File");
        try {
            startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }*/

}
