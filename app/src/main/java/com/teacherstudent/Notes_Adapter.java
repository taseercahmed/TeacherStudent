package com.teacherstudent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Notes_Adapter extends RecyclerView.Adapter<Notes_Adapter.Notes_ViewHolder> {

    Context mCtx;
    ArrayList<Notes_Setter> notesList;

    public Notes_Adapter(Context mCtx, ArrayList<Notes_Setter> notesList) {
        this.mCtx = mCtx;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public Notes_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.activity_notes_ryclr_design,parent,false);
        return new Notes_Adapter.Notes_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Notes_ViewHolder holder, int position) {
        Notes_Setter notes_setter = notesList.get(position);
        String documentDate = notes_setter.getDocumentDate();
        String documentMessage = notes_setter.getDocumentMessage();
        String documentSenderEmail = notes_setter.getDocumentSenderEmail();
        String documentTime = notes_setter.getDocumentTime();
        String documentType = notes_setter.getDocumentType();

        holder.documentDate.setText(documentDate);
        holder.documentMessage.setText(documentMessage);
        holder.documentSenderEmail.setText(documentSenderEmail);
        holder.documentTime.setText(documentTime);
        holder.documentType.setText(documentType);

        if (!holder.documentType.equals("message")) {
            holder.documentMessage.setMaxLines(1);
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class Notes_ViewHolder extends RecyclerView.ViewHolder{
        TextView documentDate,documentMessage,documentSenderEmail,documentTime,documentType;

        public Notes_ViewHolder(@NonNull View itemView) {
            super(itemView);

            documentDate = itemView.findViewById(R.id.documentDate);
            documentMessage = itemView.findViewById(R.id.documentMessage);
            documentSenderEmail = itemView.findViewById(R.id.documentSenderEmail);
            documentTime = itemView.findViewById(R.id.documentTime);
            documentType = itemView.findViewById(R.id.documentType);
        }
    }
}
