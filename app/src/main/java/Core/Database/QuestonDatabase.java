package Core.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import Core.Constants;

@Database(entities = {QuestionModel.class}, version = 1, exportSchema = false)
public abstract class QuestonDatabase extends RoomDatabase {
    public abstract QuestionDAO pinDAO();

    private static QuestonDatabase INSTANCE;

    public static QuestonDatabase getDataBase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuestonDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            QuestonDatabase.class, Constants.DB_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }

        }
        return INSTANCE;
    }
}
