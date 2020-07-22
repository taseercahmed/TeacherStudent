package com.teacherstudent.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.teacherstudent.R;

import java.util.Date;
import java.util.Map;

public class MyFirebaseMessaging extends FirebaseMessagingService {

   // private static final String channel_id = "104";
   String id = "main_channel";

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        if (remoteMessage.getData() != null) {

            Map<String, String> data = remoteMessage.getData();

            final String title = data.get("title");
            final String message = data.get("message");
            final String senderEmail = data.get("senderEmail");
            final String senderName = data.get("senderName");
         //   final String senderPhotoUrl = data.get("senderPhotoUrl");
           // final String myGender = data.get("receiverGender");
            showNotification(title, message, senderEmail, senderName);

            if (title.equals("New Message")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MyFirebaseMessaging.this, message, Toast.LENGTH_SHORT).show();

                        //showNotification(title,message,"","","");

                        //startActivity(new Intent(getBaseContext(), MessagingActivity.class));
                        //showArrivedNotification(message);
                    }
                });
            }
            if (title.equals("cancel")) {
                //notifyShow(remoteMessage);
            }
        }
    }

    private void showNotification(String title, String body, String userEmail, String userName)
    {
        // channel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Channel name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        //intent
        //Intent playstoreBtnIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/"));
        Intent intent = new Intent(this, Notification.class);
        intent.putExtra("userEmail", userEmail);
       // intent.putExtra("userImageUrl", userPhotoUri);
        intent.putExtra("userName", userName);
     //   intent.putExtra("myGender", myGender);


        PendingIntent resultpendingIntent = PendingIntent.getActivity(
                this,
                1,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, id);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle(title);
        //notificationBuilder.setContentText(Html.fromHtml("<b>"+userName + ": " + "</b>" + body));
        notificationBuilder.setContentText(body);

        notificationBuilder.setLights(Color.BLUE, 10, 10);
        notificationBuilder.setColor(Color.GREEN);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
        notificationBuilder.setAutoCancel(true);

        //for opening playstore
        notificationBuilder.setContentIntent(resultpendingIntent);


        // for multiple notifications
        //Random random = new Random();
        //int m = random.nextInt(9999 - 1000) + 1000;
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(m, notificationBuilder.build());
        /*market://details?id=*/
    }

    public void showArrivedNotification(String body) {

        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(),
                0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Sucessssssss")
                .setContentText(body)
                .setContentIntent(contentIntent);
        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        //String user = remoteMessage.getData().get("user");
        //String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("message");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        //int j = Integer.parseInt(user.replaceAll("[\\D]",""));
//        Intent intent = new Intent(this, TabActivity.class);
//        Bundle bundle = new Bundle();
//        //bundle.putString("userid",user);
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                //.setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound);
        //.setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        /*if (j>0){
            i = j;
        }*/

        noti.notify(i, builder.build());
    }

//    private void notifyShow(RemoteMessage remoteMessage) {
//        //Map<String, String> data = remoteMessage.getData();
//        //
//        // NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
//        // style.bigPicture();
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager1 = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel chan1 = new NotificationChannel(
//                    channel_id,
//                    "default",
//                    NotificationManager.IMPORTANCE_NONE);
//            chan1.setLightColor(Color.TRANSPARENT);
//            chan1.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//            notificationManager1.createNotificationChannel(chan1);
//        }
//
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "104";
////        Intent intent = new Intent(getApplicationContext(), TabActivity.class);
//        //    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        //    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(remoteMessage.getData().get("title"))
//                .setAutoCancel(true)
//                //.setSound(defaultSound)
//                .setContentText(remoteMessage.getData().get("message"))
//                //  .setContentIntent(pendingIntent)
//
//                .setWhen(System.currentTimeMillis())
//                .setPriority(Notification.PRIORITY_MAX);
//
//
//        notificationManager.notify(1, notificationBuilder.build());
//    }


}
