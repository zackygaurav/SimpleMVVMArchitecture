package in.itechvalley.topmovies.repo;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import in.itechvalley.topmovies.db.AppDatabase;
import in.itechvalley.topmovies.model.MovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.model.TopMoviesRequest;
import in.itechvalley.topmovies.util.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopMoviesRepo
{
    /*
    * TAG
    * */
    private static final String TAG = "TopMoviesRepo";

    /*
     * Instance of MutableLiveData to observer API Callback Status
     * */
    private MutableLiveData<Integer> apiObserver = new MutableLiveData<>();

    /*
    * Global Instance of AppDatabase
    * */
    private AppDatabase appDatabase;

    /*
    * Constructor
    * */
    public TopMoviesRepo(AppDatabase appDatabase)
    {
        this.appDatabase = appDatabase;
    }

    /*
    * This is a helper method to fetch Movies and store it into Database
    * */
    public void requestMoviesList()
    {
        /*
         * OkHttp Logging Interceptor
         * */
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://api.myjson.com/");

        Retrofit retrofit = retrofitBuilder.build();

        TopMoviesRequest topMoviesRequest = retrofit.create(TopMoviesRequest.class);
        Call<TopMoviesModel> request = topMoviesRequest.getMoviesData();
        request.enqueue(new Callback<TopMoviesModel>()
        {
            @Override
            public void onResponse(@NonNull Call<TopMoviesModel> call, @NonNull Response<TopMoviesModel> response)
            {
                TopMoviesModel responseBody = response.body();
                if (responseBody == null)
                {
                    apiObserver.postValue(Constants.StatusCodes.ERROR_CODE_RESPONSE_BODY_NULL);
                    return;
                }

                /*
                 * Get the List from Response
                 * */
                List<MovieModel> moviesList = responseBody.getMovieModelList();
                if (moviesList.size() == 0)
                {
                    apiObserver.postValue(Constants.StatusCodes.ERROR_CODE_RESPONSE_FAILED);
                }
                else
                {
                    apiObserver.postValue(Constants.StatusCodes.STATUS_CODE_SUCCESS);
                }

                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        /*
                         * Attempt to cache the data locally
                         * */
                        long[] results = appDatabase.getTopMoviesDao().cacheMoviesData(moviesList);

                        for (long singleResult : results)
                        {
                            Log.i(TAG, String.format("Inserted with Row ID %d", singleResult));
                        }
                    }
                });

                thread.start();
            }

            @Override
            public void onFailure(@NonNull Call<TopMoviesModel> call, @NonNull Throwable throwable)
            {
                if (throwable instanceof UnknownHostException)
                {
                    apiObserver.postValue(Constants.StatusCodes.ERROR_CODE_UNKNOWN_HOST_EXCEPTION);
                }
                else if (throwable instanceof SocketTimeoutException)
                {
                    apiObserver.postValue(Constants.StatusCodes.ERROR_CODE_SOCKET_TIMEOUT);
                }
                else
                {
                    apiObserver.postValue(Constants.StatusCodes.ERROR_CODE_FAILURE_UNKNOWN);
                }

                Log.e(TAG, "Failed to fetch Movies List", throwable);
            }
        });
    }
}
