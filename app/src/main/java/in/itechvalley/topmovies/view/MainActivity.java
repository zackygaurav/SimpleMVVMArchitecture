package in.itechvalley.topmovies.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.itechvalley.topmovies.R;
import in.itechvalley.topmovies.adapter.TopMoviesRecyclerAdapter;
import in.itechvalley.topmovies.model.MovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;
import in.itechvalley.topmovies.model.TopMoviesRequest;
import in.itechvalley.topmovies.util.Constants;
import in.itechvalley.topmovies.util.Tools;
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

    @BindView(android.R.id.content)
    View currentView;
    
    @BindView(R.id.recycler_activity_main)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_activity_main)
    SwipeRefreshLayout swipeRefreshLayout;

    /*
    * Global Instance of ViewModel
    * */
    private TopMoviesViewModel viewModel;

    /*
     * List for RecyclerView's Adapter
     * */
    private List<MovieModel> topMoviesModelsList;

    /*
    * Global Instance of Adapter
    * */
    private TopMoviesRecyclerAdapter adapter;

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
         * Trigger SwipeRefreshLayout
         * */
        swipeRefreshLayout.setRefreshing(true);

        /*
        * Attach onRefresh method to swipeRefreshLayout
        * */
        swipeRefreshLayout.setOnRefreshListener(this);

        initAdapter();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        /*
        * Observe for Movies List
        * */
        viewModel.getListObserver().observe(this, new Observer<List<MovieModel>>()
        {
            @Override
            public void onChanged(List<MovieModel> movieModels)
            {
                /*
                * Check for Null
                * */
                if (movieModels != null)
                {
                    /*
                    * Check Empty Condition
                    * */
                    if (movieModels.size() > 0)
                    {
                        adapter.refreshRecyclerView(movieModels);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "List is empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /*
        * Observe for API Callback
        * */
        viewModel.getApiObserver().observe(this, new Observer<Integer>()
        {
            @Override
            public void onChanged(Integer statusCode)
            {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);

                switch (statusCode)
                {
                    case Constants.StatusCodes.STATUS_CODE_SUCCESS:
                    {
                        break;
                    }

                    case Constants.StatusCodes.ERROR_CODE_RESPONSE_FAILED:
                    {
                        /*
                         * This means Request was processed successfully but value of success is false
                         * */
                        Tools.showSnackbar(currentView, "Failed to load Movies List. Please try again. \uD83E\uDD10", Snackbar.LENGTH_INDEFINITE);

                        break;
                    }

                    case Constants.StatusCodes.ERROR_CODE_RESPONSE_BODY_NULL:
                    {
                        /*
                         * This means the Response is NULL
                         * */
                        Tools.showSnackbar(currentView, "Server Error Occurred. Please try again. \uD83E\uDD10", Snackbar.LENGTH_INDEFINITE);

                        break;
                    }

                    case Constants.StatusCodes.ERROR_CODE_UNKNOWN_HOST_EXCEPTION:
                    {
                        /*
                         * This means the onFailure was called and UnknownHostException occurred.
                         * */
                        Tools.showSnackbar(currentView, "No Internet Connection. Please check your Internet connection and try again. \uD83D\uDCE1", Snackbar.LENGTH_INDEFINITE);

                        break;
                    }

                    case Constants.StatusCodes.ERROR_CODE_SOCKET_TIMEOUT:
                    {
                        /*
                         * This means the onFailure was called and SocketTimeout occurred.
                         * */
                        Tools.showSnackbar(currentView, "Request Timeout. Please check your Network Connectivity and try again. \u23F3", Snackbar.LENGTH_INDEFINITE);

                        break;
                    }

                    case Constants.StatusCodes.ERROR_CODE_FAILURE_UNKNOWN:
                    {
                        /*
                         * This means the onFailure was called due to some error. Error is Unknown. Check LogCat for details.
                         * */
                        Tools.showSnackbar(currentView, "Some error Occurred. Please try again. \uD83D\uDE15", Snackbar.LENGTH_INDEFINITE);

                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh()
    {
        /*
        * Trigger SwipeRefreshLayout
        * */
        swipeRefreshLayout.setRefreshing(true);

        viewModel.requestMoviesList();
    }

    private void initAdapter()
    {
        /*
         * 1. Init List
         * */
        topMoviesModelsList = new ArrayList<>();

        /*
         * 2. Init Adapter
         * */
        adapter = new TopMoviesRecyclerAdapter(topMoviesModelsList);

        /*
        * Set Adapter to RecylerView
        * */
        recyclerView.setAdapter(adapter);
    }
}
