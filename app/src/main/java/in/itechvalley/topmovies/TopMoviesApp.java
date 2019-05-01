package in.itechvalley.topmovies;

import android.app.Application;

import in.itechvalley.topmovies.db.AppDatabase;
import in.itechvalley.topmovies.repo.TopMoviesRepo;

public class TopMoviesApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public TopMoviesRepo provideRepo()
    {
        return new TopMoviesRepo(provideAppDatabase());
    }

    private AppDatabase provideAppDatabase()
    {
        return AppDatabase.getInstance(getBaseContext());
    }
}
