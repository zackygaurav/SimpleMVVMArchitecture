package in.itechvalley.topmovies.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.itechvalley.topmovies.R;
import in.itechvalley.topmovies.model.MovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.model.TopMoviesRequest;
import in.itechvalley.topmovies.viewmodel.TopMoviesViewModel;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
{
    /*
    * TAG
    * */
    private static final String TAG = "MainActivity";

    @BindView(R.id.swipe_refresh_activity_main)
    SwipeRefreshLayout swipeRefreshLayout;

    /*
    * Global Instance of ViewModel
    * */
    private TopMoviesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * ButterKnife Init
        * */
        ButterKnife.bind(this);

        /*
        * Init the Global Instance of ViewModel
        * */
        viewModel = ViewModelProviders.of(this).get(TopMoviesViewModel.class);
        viewModel.requestMoviesList();

        /*
        * Attach onRefresh method to swipeRefreshLayout
        * */
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        /*
         * Observer for DataChanges
         * */
        LiveData<String> moviesDataObserver = viewModel.getDataObserver();
        moviesDataObserver.observe(this, new Observer<String>()
        {
            @Override
            public void onChanged(String value)
            {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);

                if (value != null)
                {
                    Toast.makeText(MainActivity.this, String.format("Movies Size: %s", value), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRefresh()
    {
        viewModel.requestMoviesList();
    }
}
