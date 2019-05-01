package in.itechvalley.topmovies.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import in.itechvalley.topmovies.model.MovieModel;

@Database(entities = {MovieModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    private static final String DATABASE_NAME = "db_top_movie.db";

    private static AppDatabase INSTANCE;

    /*
    * Getters for Dao
    * */
    public abstract TopMoviesDao getTopMoviesDao();

    /*
    * Singleton Approach
    * */
    public static AppDatabase getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        }

        return INSTANCE;
    }
}
