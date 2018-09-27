package com.example.hovhannesstepanyan.askhovo;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hovhannesstepanyan.askhovo.Notification.QuestionNotification;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.MaterialMultiAutoCompleteTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Core.Constants;
import Core.Database.QuestionModel;
import Core.Database.QuestonDatabase;
import Core.Pickers.DateTimePicker;

public class CreateQuestionActivity extends AppCompatActivity {

    public static final String TAG = "CreateQuestionActivity";

    private AppCompatButton mSaveButton;
    private MaterialMultiAutoCompleteTextView mDescriptionView;
    private MaterialEditText mQuestionView;
    private TextView mDateTextView;
    private Switch mSwitch;
    private QuestionModel mQuestion;
    private Calendar mSelectCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mQuestion = bundle.getParcelable(Constants.QUESTION_MODEL);
            if (mQuestion != null) {
                mQuestionView.setText(mQuestion.getTitle());
                mDescriptionView.setText(mQuestion.getQuestion());
                mDateTextView.setText(mQuestion.getDate());
                mSwitch.setChecked(mQuestion.getNotificationEnabled());
            }
        }


        mSaveButton.setOnClickListener(view -> {
            if (mQuestionView.getText() != null && mDescriptionView.getText() != null) {
                String question = mQuestionView.getText().toString();
                String descr = mDescriptionView.getText().toString();
                if (question.isEmpty()) {
                    mQuestionView.setError("Question must not be empty");
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
                        QuestionModel questionModel = new QuestionModel(descr, question, SimpleDateFormat.getInstance().format(new Date()), false, mSwitch.isChecked());
                        QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().insertQuestion(questionModel);
                        if (mSelectCalendar != null && mSwitch.isChecked()) {
                            Calendar now = Calendar.getInstance();
                            if (mSelectCalendar.before(now)) {
                                createAlertDialog(((dialog, which) -> {
                                    QuestionNotification.setReminder(CreateQuestionActivity.this, NotificationReceiver.class, questionModel, mSelectCalendar);
                                    Date selectDate = mSelectCalendar.getTime();
                                    questionModel.setDate(SimpleDateFormat.getInstance().format(selectDate));
                                    QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().updateQuestion(questionModel);
                                    finish();
                                }));
                            } else {
                                QuestionNotification.setReminder(CreateQuestionActivity.this, NotificationReceiver.class, questionModel, mSelectCalendar);
                                Date selectDate = mSelectCalendar.getTime();
                                questionModel.setDate(SimpleDateFormat.getInstance().format(selectDate));
                                QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().updateQuestion(questionModel);
                                finish();
                            }
                        } else {
                            QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().updateQuestion(questionModel);
                            finish();
                        }
                    } else {
                        mQuestion.setQuestion(descr);
                        mQuestion.setTitle(question);
                        mQuestion.setNotificationEnabled(mSwitch.isChecked());
                        if (mSelectCalendar != null && mSwitch.isChecked()) {
                            Calendar now = Calendar.getInstance();
                            if (mSelectCalendar.before(now)) {
                                createAlertDialog((dialog, which) -> {
                                    mSelectCalendar.set(Calendar.DAY_OF_MONTH, mSelectCalendar.get(Calendar.DAY_OF_MONTH) + 1);
                                    QuestionNotification.setReminder(CreateQuestionActivity.this, NotificationReceiver.class, mQuestion, mSelectCalendar);
                                    Date selectDate = mSelectCalendar.getTime();
                                    mQuestion.setDate(SimpleDateFormat.getInstance().format(selectDate));
                                    QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().updateQuestion(mQuestion);
                                    finish();
                                });
                            } else {
                                QuestionNotification.setReminder(CreateQuestionActivity.this, NotificationReceiver.class, mQuestion, mSelectCalendar);
                                Date selectDate = mSelectCalendar.getTime();
                                mQuestion.setDate(SimpleDateFormat.getInstance().format(selectDate));
                                QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().updateQuestion(mQuestion);
                                finish();
                            }
                        } else {
                            QuestonDatabase.getDataBase(getApplicationContext()).pinDAO().updateQuestion(mQuestion);
                            finish();
                        }
                    }
                }
            }
        });

    }

    private void createAlertDialog(DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(CreateQuestionActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(CreateQuestionActivity.this);
        }
        builder.setIcon(R.mipmap.ic_launcher_app)
                .setMessage("Error e exe")
                .setTitle("Errrrrrrrrorrrrrr")
                .setPositiveButton("ok", listener)
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .create()
                .show();
    }

    private void bindViews() {
        setContentView(R.layout.activity_create_question);
        mSaveButton = findViewById(R.id.b_save_question);
        mDescriptionView = findViewById(R.id.mtw_description);
        mQuestionView = findViewById(R.id.mte_question);
        mDateTextView = findViewById(R.id.tv_notification_date);
        mSwitch = findViewById(R.id.switch_enable_notification);
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
//            new DateTimePicker().show(getSupportFragmentManager(), TAG);
//            QuestionNotification.setReminder(this, NotificationReceiver.class, mQuestion, 2018, 9, 23, 14, 47);
            new DateTimePicker()
                    .setListener((view, calendar) -> {
                        mSelectCalendar = calendar;
                        mDateTextView.setText(SimpleDateFormat.getInstance().format(calendar.getTime()));
                    })
                    .show(getSupportFragmentManager(), TAG);
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

    private void postNotification() {
        Notification notification = new NotificationCompat.Builder(this, AskApplication.CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_app)
                .setContentTitle(mQuestion.getTitle())
                .setContentText(mQuestion.getQuestion())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();
        NotificationManagerCompat mManager = NotificationManagerCompat.from(this);
        mManager.notify(1, notification);
    }

}
