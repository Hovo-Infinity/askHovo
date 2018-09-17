package Core.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface QuestionDAO {

    @Query("SELECT * FROM QUESTION_TABLE")
    List<QuestionModel> getAllQuestions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(final QuestionModel model);

    @Update
    void updateQuestion(final QuestionModel model);

    @Query("SELECT * FROM question_table WHERE id=:id LIMIT 1")
    QuestionModel selectQuestion(final int id);
}
