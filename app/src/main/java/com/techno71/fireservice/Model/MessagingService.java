package com.techno71.fireservice.Model;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techno71.fireservice.BuildConfig;
import com.techno71.fireservice.R;
import com.techno71.fireservice.View.CompanyMapsActivity;
import com.techno71.fireservice.View.FireMapActivity;
import com.techno71.fireservice.View.UserMapActivity;
import java.io.File;
import java.util.Map;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MessagingService extends FirebaseMessagingService {

    NotificationManager mNotificationManager;

    public final static int ALARM_ID = 12345;
    Intent resultIntent;
    String user_type="";

    SharedPreferences sharedPreferences_type;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        user_type=sharedPreferences_type.getString("user_type","not found");

         String data=remoteMessage.getNotification().getBody();

         String result[]=  data.split(",,");

         String latitude=result[0];

         String longtude=result[1];

        sharedPreferences_type.edit().putFloat("latitude001", Float.parseFloat(latitude)).commit();
        sharedPreferences_type.edit().putFloat("longitude001", Float.parseFloat(longtude)).commit();

         Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        // playing audio and vibration when user se reques
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
        r.play();
      //  r.setLooping(true,);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setSmallIcon(R.drawable.icontrans);
            builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
        } else {
//            builder.setSmallIcon(R.drawable.icon_kritikar);
            builder.setSmallIcon(R.drawable.ic_baseline_notifications_24);
        }

       if (user_type.contains("1")){

            resultIntent = new Intent(this, FireMapActivity.class);

        }else {

            resultIntent = new Intent(this, UserMapActivity.class);
        }

       // Intent resultIntent = new Intent(this, UserMapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_MUTABLE);
        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        //builder.setWhen(System.currentTimeMillis());

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();

        //  builder.setTimeoutAfter(startTime);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);

            channel.setSound(soundUri,audioAttributes);
            channel.enableLights(true);
            channel.enableVibration(true);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }

// notificationId is a unique int for each notification that you must define
        mNotificationManager.notify(100, builder.build());

    }
}
