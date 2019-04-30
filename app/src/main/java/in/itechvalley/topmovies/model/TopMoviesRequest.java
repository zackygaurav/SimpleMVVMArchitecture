package in.itechvalley.topmovies.model;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TopMoviesRequest
{
    @GET("bins/18buhu")
    Call<TopMoviesModel> getMoviesData();
}
