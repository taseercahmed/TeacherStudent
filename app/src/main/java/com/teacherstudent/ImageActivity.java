package com.teacherstudent;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImageActivity extends AppCompatActivity {
ImageView imageView;
    Intent intent1=getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().setTitle("Downloads");
        imageView=findViewById(R.id.imageView7);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        Glide.with(this).load(url).into(imageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.down){
            Toast.makeText(this,"Downloading",Toast.LENGTH_LONG).show();
            Intent intent=getIntent();
            String url=intent.getStringExtra("url");
            String destinationDirectory=intent.getStringExtra("des");
            String fileName=intent.getStringExtra("filename");
            String fileExtension=intent.getStringExtra("exten");

            DownloadManager downloadmanager = (DownloadManager) this.
                    getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(ImageActivity.this, destinationDirectory, fileName + fileExtension);

            downloadmanager.enqueue(request);
        }
        return true;
    }
}
