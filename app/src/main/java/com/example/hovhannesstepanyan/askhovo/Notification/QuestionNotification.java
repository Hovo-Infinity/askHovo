package com.example.hovhannesstepanyan.askhovo.Notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.hovhannesstepanyan.askhovo.AskApplication;
import com.example.hovhannesstepanyan.askhovo.R;

import java.util.Calendar;

import Core.Constants;
import Core.Database.QuestionModel;

public class QuestionNotification {

    private static final int DAILY_REMINDER_REQUEST_CODE = 1000;
    public static final String TAG = "QuestionNotification";

    public static void showNotification(Context context, Class<?> cls, QuestionModel question) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(cls);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, AskApplication.CHANNEL_ID);

        Notification notification = builder.setContentTitle(question.getTitle())
                    .setContentText(question.getQuestion())
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setSmallIcon(R.mipmap.ic_stat_panda)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_app))
                    .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(question.getId(), notification);
        }

    }

    public static void cancelReminder(Context context, Class<?> cls) {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.cancel(pendingIntent);
        }
        pendingIntent.cancel();
    }

    public static void setReminder(Context context, Class<?> cls, final QuestionModel model, Calendar setCalendar) {

        cancelReminder(context,cls);

        // Enable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent = new Intent(context, cls);
//        intent.putExtra(Constants.QUESTION, model);
        intent.putExtra(Constants.QUESTION_ID, model.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void setReminder(Context context, Class<?> cls, final QuestionModel model, int year, int month, int day, int hour, int minute) {

        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.YEAR, year);
        setCalendar.set(Calendar.MONTH, month);
        setCalendar.set(Calendar.DAY_OF_MONTH, day);
        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
        setCalendar.set(Calendar.MINUTE, minute);
        setCalendar.set(Calendar.SECOND, 0);

        setReminder(context, cls, model, setCalendar);
    }
}
