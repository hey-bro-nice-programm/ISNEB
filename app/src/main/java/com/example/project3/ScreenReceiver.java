package com.example.project3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;

public class ScreenReceiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "ISNEB channel";

    NotificationManager mNotificationManager;
    public ScreenReceiver() {
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            String DEBUG_TAG = "ScreenReceiver";
            Log.d(DEBUG_TAG,"Screen is off.");
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.cancelAll();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            String DEBUG_TAG = "ScreenReceiver";

            DBWords mDBConnector = new DBWords(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = context.getString(R.string.channel_name);
                String description = context.getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            int CNT_OF_WORDS = mDBConnector.SizeOfDB();
            Log.d(DEBUG_TAG, CNT_OF_WORDS + "");
            int index = (int)(Math.random() * CNT_OF_WORDS);
            Words cw = mDBConnector.select(index);
            String context_title = cw.getRu_word() + " - " + cw.getEn_word();
            String description = cw.getDescription();
            //Log.d(DEBUG_TAG, context_title);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setSmallIcon(R.drawable.invalid)
                            .setContentTitle(context_title)
                            .setContentText(description)
                            .setOngoing(true)
                            .setPriority(InfoToClasses.NotPrio);
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.notify(NOTIFY_ID, builder.build());

        }
        else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            String DEBUG_TAG = "ScreenReceiver";
            Log.d(DEBUG_TAG,"User present???");
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(context);
            notificationManager.cancelAll();
        }
    }

}