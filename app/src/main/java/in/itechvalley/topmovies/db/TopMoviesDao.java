package in.itechvalley.topmovies.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import in.itechvalley.topmovies.model.MovieModel;

@Dao
public interface TopMoviesDao
{
    /*
    * int
    * long, long[]
    * LiveData
    * List
    * */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] cacheMoviesData(List<MovieModel> movieModelsList);

    @Query("SELECT * FROM table_movies")
    LiveData<List<MovieModel>> readCachedMoviesData();

    @Query("DELETE FROM table_movies")
    int truncateTable();
}
