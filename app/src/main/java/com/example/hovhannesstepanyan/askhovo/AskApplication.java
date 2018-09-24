package com.example.hovhannesstepanyan.askhovo;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import Core.MGPrefsCacheManager;

public class AskApplication extends Application {

    public static final String CHANNEL_ID = "askhovo.channel.1";

    @Override
    public void onCreate() {
        super.onCreate();
        MGPrefsCacheManager.getInstance().Initialize(getApplicationContext());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Reminder",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("For notify about questions");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
