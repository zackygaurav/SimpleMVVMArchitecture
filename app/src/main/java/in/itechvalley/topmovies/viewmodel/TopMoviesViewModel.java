package in.itechvalley.topmovies.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import in.itechvalley.topmovies.TopMoviesApp;
import in.itechvalley.topmovies.model.MovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.model.TopMoviesRequest;
import in.itechvalley.topmovies.repo.TopMoviesRepo;
import in.itechvalley.topmovies.util.Constants;
import in.itechvalley.topmovies.view.MainActivity;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopMoviesViewModel extends AndroidViewModel
{
    private static final String TAG = "TopMoviesViewModel";

    /*
    * Inject the Instance of TopMoviesRepo
    * */
    private TopMoviesRepo topMoviesRepo;

    /*
     * Constructor
     * */
    public TopMoviesViewModel(@NonNull Application application)
    {
        super(application);

        /*
         * Init the Instance of TopMoviesRepo
         * */
        topMoviesRepo = ((TopMoviesApp) application).provideRepo();
    }

    public void requestMovieList()
    {
        topMoviesRepo.requestMoviesList();
    }

    /*
    * Getter to return the Instance of listObserver
    * */
    public LiveData<List<MovieModel>> getListObserver()
    {
        return topMoviesRepo.getMediatorLiveData();
    }

    public LiveData<Integer> getApiObserver()
    {
        return topMoviesRepo.getApiObserver();
    }
}
