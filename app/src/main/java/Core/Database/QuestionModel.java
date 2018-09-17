package Core.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Core.Constants;

@Entity(tableName = Constants.DB_NAME)
public class QuestionModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String  question;

    @NonNull
    private String title;
    @NonNull
    private String date;

    private Boolean compleated;

    public QuestionModel(String question, String title, String date, Boolean compleated) {
        this.question = question;
        this.title = title;
        this.date = date;
        this.compleated = compleated;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getTitle() {
        return title;
    }

    public Date getFormatedDate() {
        try {
            return SimpleDateFormat.getInstance().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDate() {
        return date;
    }

    public Boolean getCompleated() {
        return compleated;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCompleated(Boolean compleated) {
        this.compleated = compleated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.question);
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeValue(this.compleated);
    }

    protected QuestionModel(Parcel in) {
        this.id = in.readInt();
        this.question = in.readString();
        this.title = in.readString();
        this.date = in.readString();
        this.compleated = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<QuestionModel> CREATOR = new Parcelable.Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel source) {
            return new QuestionModel(source);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };
}
