package com.teacherstudent.Student;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teacherstudent.ImageActivity;
import com.teacherstudent.R;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class S_Notes_Adapter extends RecyclerView.Adapter<S_Notes_Adapter.S_Notes_ViewHolder>  {

    Context mCtx;
    ArrayList<S_Notes_Setter> notesList;

    public S_Notes_Adapter(Context mCtx, ArrayList<S_Notes_Setter> notesList) {
        this.mCtx = mCtx;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public S_Notes_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_s_notes_rcyclr_design1,parent,false);
        return new S_Notes_Adapter.S_Notes_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final S_Notes_ViewHolder holder, int position) {
        S_Notes_Setter s_notes_setter = notesList.get(position);

        final String msgSenderEmail = s_notes_setter.getS_Notes_senderEmail();
        final String msgType = s_notes_setter.getS_Notes_msgType();
        final String msgDetail = s_notes_setter.getS_Notes_messageDetail();
        String msgTime = s_notes_setter.getS_Notes_time();
        String msgDate = s_notes_setter.getS_Notes_date();
    //    Toast.makeText(mCtx,"name is"+msgSenderEmail,Toast.LENGTH_LONG).show();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Teacher");

        // final String finalName = name;
        ref.child(msgSenderEmail.replace(".","_dot_")).child("Profile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String namess =dataSnapshot.child("name").getValue().toString();

                        holder.S_Notes_senderEmail.setText(namess);

                        //  }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        if (msgType.contains("jpg") || msgType.contains("png") || msgType.contains("jpeg") || msgType.contains("message")){
            if (msgType.contains("jpg") || msgType.contains("png") || msgType.contains("jpeg")){
                //image
               // holder.S_Notes_fileDownloadBtn.setVisibility(View.GONE);
                holder.S_Notes_message.setVisibility(View.GONE);
                holder.S_Notes_image.setVisibility(View.VISIBLE);
              //  holder.S_Notes_imgDownloadBtn.setVisibility(View.VISIBLE);
                Glide.with(mCtx).load(msgDetail).into(holder.S_Notes_image);
            }else {
                // message
             //   holder.S_Notes_fileDownloadBtn.setVisibility(View.GONE);
                holder.S_Notes_message.setVisibility(View.VISIBLE);
                holder.S_Notes_image.setVisibility(View.GONE);
               // holder.S_Notes_imgDownloadBtn.setVisibility(View.GONE);
                holder.S_Notes_message.setText(msgDetail);

                holder.S_Notes_message.setSingleLine(false);
            }
        }else {
            // document
          //  holder.S_Notes_fileDownloadBtn.setVisibility(View.VISIBLE);
            holder.S_Notes_message.setVisibility(View.VISIBLE);
            holder.S_Notes_image.setVisibility(View.GONE);
          //  holder.S_Notes_imgDownloadBtn.setVisibility(View.GONE);

            holder.S_Notes_message.setText(msgDetail);
            holder.S_Notes_message.setSingleLine(true);
        }


      //  holder.S_Notes_msgType.setText(msgType);
        holder.S_Notes_time.setText(msgTime);
        holder.S_Notes_date.setText(msgDate);
        holder.S_Notes_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgType.contains("jpg") || msgType.contains("png") || msgType.contains("jpeg")){
                    Intent intent=new Intent(mCtx, ImageActivity.class);
                    intent.putExtra("url",msgDetail);
                    intent.putExtra("des",DIRECTORY_DOWNLOADS);
                    intent.putExtra("exten",msgType);
                    intent.putExtra("filename",msgDetail);
                    intent.putExtra("name",msgSenderEmail);
                    mCtx.startActivity(intent);
                }else{
                  //
                }

            }
        });

        holder.S_Notes_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgType.contains("message"))
                {

                }else{
                    downloadFile(mCtx,msgDetail,msgType,DIRECTORY_DOWNLOADS,msgDetail);
                }
            }
        });


//        holder.S_Notes_imgDownloadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

//            }
//        });

//        holder.S_Notes_fileDownloadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                downloadFile(mCtx,msgDetail,msgType,DIRECTORY_DOWNLOADS,msgDetail);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class S_Notes_ViewHolder extends RecyclerView.ViewHolder{
        TextView S_Notes_senderEmail,S_Notes_msgType,S_Notes_message,S_Notes_time,S_Notes_date;
        ImageView S_Notes_image,S_Notes_imgDownloadBtn,S_Notes_fileDownloadBtn;

        public S_Notes_ViewHolder(@NonNull View itemView) {
            super(itemView);

            S_Notes_senderEmail = itemView.findViewById(R.id.S_Notes_senderEmail);
           // S_Notes_msgType = itemView.findViewById(R.id.S_Notes_msgType);
            S_Notes_message = itemView.findViewById(R.id.S_Notes_message);
            S_Notes_image = itemView.findViewById(R.id.S_Notes_image);
            S_Notes_time = itemView.findViewById(R.id.S_Notes_time);
            S_Notes_date = itemView.findViewById(R.id.S_Notes_date);
           // S_Notes_imgDownloadBtn = itemView.findViewById(R.id.S_Notes_imgDownloadBtn);
          //  S_Notes_fileDownloadBtn = itemView.findViewById(R.id.S_Notes_fileDownloadBtn);
        }
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
