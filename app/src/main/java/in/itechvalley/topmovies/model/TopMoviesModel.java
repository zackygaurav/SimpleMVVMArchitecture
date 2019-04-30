package in.itechvalley.topmovies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TopMoviesModel implements Serializable
{
    @SerializedName("movies")
    private List<MovieModel> movieModelList;

    /*
    * Constructor
    * */
    public TopMoviesModel(List<MovieModel> movieModelList)
    {
        this.movieModelList = movieModelList;
    }

    /*
    * Getters
    * */
    public List<MovieModel> getMovieModelList()
    {
        return movieModelList;
    }
}
