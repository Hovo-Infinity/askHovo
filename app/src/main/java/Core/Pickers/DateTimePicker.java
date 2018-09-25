package Core.Pickers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class DateTimePicker implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Calendar mSelectedCalendar;
    private FragmentManager mFragmentManager;
    private String mTag;
    private DateTimePickdrSetListener mListener;

    public DateTimePicker() {
        mSelectedCalendar = Calendar.getInstance();
    }

    public DateTimePicker setListener(DateTimePickdrSetListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public void show(@NonNull FragmentManager manager, String tag) {
        mFragmentManager = manager;
        mTag = tag;
        new DatePickerFragment()
                .setListener(this)
                .show(manager, tag);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mSelectedCalendar.set(Calendar.YEAR, year);
        mSelectedCalendar.set(Calendar.MONTH, month);
        mSelectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        new TimePickerFragment()
                .setListener(this)
                .show(mFragmentManager, mTag);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mSelectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mSelectedCalendar.set(Calendar.MINUTE, minute);
        mListener.onDateTimeSelected(this, mSelectedCalendar);
    }

    public interface DateTimePickdrSetListener {
        void onDateTimeSelected(DateTimePicker view, Calendar calendar);
    }
}
