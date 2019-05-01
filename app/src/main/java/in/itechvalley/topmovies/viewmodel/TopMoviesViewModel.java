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

import in.itechvalley.topmovies.model.MovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.model.TopMoviesRequest;
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
    * Instance of MutableLiveData to observe List<MovieModel>
    * */
    private MutableLiveData<List<MovieModel>> listObserver = new MutableLiveData<>();

    /*
    * Instance of MutableLiveData to observer API Callback Status
    * */
    private MutableLiveData<Integer> apiObserver = new MutableLiveData<>();

    /*
     * Constructor
     * */
    public TopMoviesViewModel(@NonNull Application application)
    {
        super(application);
    }

    /*
    * Getter to return the Instance of listObserver
    * */
    public LiveData<List<MovieModel>> getListObserver()
    {
        return listObserver;
    }

    public LiveData<Integer> getApiObserver()
    {
        return apiObserver;
    }

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

                /*
                * Post the List to it's observer
                * */
                listObserver.postValue(moviesList);
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
