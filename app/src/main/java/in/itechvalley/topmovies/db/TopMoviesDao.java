package in.itechvalley.topmovies.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

import in.itechvalley.topmovies.model.MovieModel;

@Dao
public interface TopMoviesDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] cacheMoviesData(List<MovieModel> movieModelsList);
}
