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

import java.util.List;

import in.itechvalley.topmovies.model.MovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.model.TopMoviesRequest;
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
    * Instance of LiveData
    * */
    private MutableLiveData<String> dataObserver = new MutableLiveData<>();

    /*
     * Constructor
     * */
    public TopMoviesViewModel(@NonNull Application application)
    {
        super(application);
    }

    public LiveData<String> getDataObserver()
    {
        return dataObserver;
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
                    dataObserver.postValue("Response is null");
                    return;
                }

                List<MovieModel> moviesList = responseBody.getMovieModelList();
                dataObserver.postValue(String.valueOf(moviesList.size()));
            }

            @Override
            public void onFailure(@NonNull Call<TopMoviesModel> call, @NonNull Throwable throwable)
            {
                Log.e(TAG, "Failed to fetch Movies List", throwable);

                dataObserver.postValue(throwable.getMessage());
            }
        });
    }
}
