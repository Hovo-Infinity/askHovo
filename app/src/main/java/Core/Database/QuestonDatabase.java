package Core.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import Core.Constants;

@Database(entities = {QuestionModel.class}, version = 2, exportSchema = false)
public abstract class QuestonDatabase extends RoomDatabase {
    public abstract QuestionDAO pinDAO();

    private static QuestonDatabase INSTANCE;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("ALTER TABLE QUESTION_TABLE ADD COLUMN notificationEnabled INTEGER DEFAULT(0)");
        }
    };

    public static QuestonDatabase getDataBase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuestonDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            QuestonDatabase.class, Constants.DB_NAME)
                            .allowMainThreadQueries()
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }

        }
        return INSTANCE;
    }
}
