package com.example.hovhannesstepanyan.askhovo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.MaterialMultiAutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Core.Constants;
import Core.Database.QuestionModel;
import Core.Database.QuestonDatabase;
import Core.Notification.NotificationReceiver;
import Core.Notification.QuestionNotification;
import Core.Pickers.DatePickerFragment;
import Core.Pickers.TimePickerFragment;

public class CreateQuestionActivity extends AppCompatActivity {

    private AppCompatButton mSaveButton;
    private MaterialMultiAutoCompleteTextView mDescriptionView;
    private MaterialEditText mQuestionview;
    private QuestionModel mQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reate_question);
        mSaveButton = findViewById(R.id.b_save_question);
        mDescriptionView = findViewById(R.id.mtw_description);
        mQuestionview = findViewById(R.id.mte_question);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mQuestion = bundle.getParcelable(Constants.QUESTION_MODEL);
            if (mQuestion != null) {
                mQuestionview.setText(mQuestion.getTitle());
            }
            if (mQuestion != null) {
                mDescriptionView.setText(mQuestion.getQuestion());
            }
        }


        mSaveButton.setOnClickListener(view -> {
            if (mQuestionview.getText() != null && mDescriptionView.getText() != null) {
                String question = mQuestionview.getText().toString();
                String descr = mDescriptionView.getText().toString();
                if (question.isEmpty()) {
                    mQuestionview.setError("Question must not be empty");
                }
                if (descr.isEmpty()) {
                    mDescriptionView.setError("Description must not be empty");
                }
                if (!descr.isEmpty() && !question.isEmpty()) {
                    if (mQuestion == null) {
                    /*
                    Intent resIntent = new Intent();
                    resIntent.putExtra(Constants.QUESTION, question);
                    resIntent.putExtra(Constants.DESCRIPTION, descr);
                    setResult(RESULT_OK, resIntent);
                    */
                        QuestionModel questionModel = new QuestionModel(descr, question, SimpleDateFormat.getInstance().format(new Date()), false);
                        QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().insertQuestion(questionModel);
                    } else {
                        mQuestion.setQuestion(descr);
                        mQuestion.setTitle(question);
                        QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().updateQuestion(mQuestion);
                    }
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_set_notification) {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.SECOND, 2);
            postNotification();
            return true;
        }

        /*
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void postNotification() {
        final String channelId = "my.channel.id";
        Notification.Builder builder = new Notification.Builder(this, channelId);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.androidauthority.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher_app);
        builder.setContentTitle("Questin");
        builder.setContentText("question");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(001, builder.build());
    }

    private void scheduleNotification(Notification notification, Date alarmTime) {

        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTime(), pendingIntent);
        }
    }

    private Notification getNotification() {
        int colour = getNotificationColour();
        Bitmap largeNotificationImage = getLargeNotificationImage();
        return new QuestionNotification(this).getNotification(
                "Text",
                "More text",
                R.mipmap.ic_launcher_app,
                getLargeNotificationImage(),
                colour);
    }

    private int getNotificationColour() {
        return ContextCompat.getColor(this, R.color.colorAccent);
    }

    private Bitmap getLargeNotificationImage() {
        return BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher_app);
    }
}
