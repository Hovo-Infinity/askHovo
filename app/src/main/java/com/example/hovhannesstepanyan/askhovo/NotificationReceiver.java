package com.example.hovhannesstepanyan.askhovo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.hovhannesstepanyan.askhovo.Notification.QuestionNotification;

import Core.Constants;
import Core.Database.QuestionModel;

public class NotificationReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";

    public static String NOTIFICATION = "notification";

    public static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
//                QuestionNotification.setReminder(context, NotificationReceiver.class, 2);
                return;
            }
        }

        Bundle bundle = intent.getExtras();
        assert bundle != null;
        QuestionModel questin = bundle.getParcelable(Constants.QUESTION);
        Log.e(TAG, questin.getQuestion());
        Log.e(TAG, "onReceive: ");

        //Trigger the notification
        QuestionNotification.showNotification(context, CreateQuestionActivity.class,
                questin.getTitle(), questin.getQuestion());

        /*
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        if (notificationManager != null) {
            notificationManager.notify(id, notification);
        }
        */
    }
}
