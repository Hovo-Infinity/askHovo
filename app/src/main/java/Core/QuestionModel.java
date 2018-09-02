package Core;

import java.util.Date;

public class QuestionModel {
    private int id;
    private String  question;
    private String title;
    private Date date;
    private Boolean compleated;

    public QuestionModel(int id, String question, String title, Date date, Boolean compleated) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public Boolean getCompleated() {
        return compleated;
    }

    public void checkOrUncheck() {
        compleated = !compleated;
    }
}