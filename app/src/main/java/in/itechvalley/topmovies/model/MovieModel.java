package in.itechvalley.topmovies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MovieModel implements Serializable
{
    @SerializedName("Title")
    private String title;

    @SerializedName("Year")
    private String year;

    @SerializedName("Released")
    private String released;

    @SerializedName(value = "Director", alternate = "Director ")
    private String director;

    /*
    * Constructor
    * */
    public MovieModel(String title, String year, String released, String director)
    {
        this.title = title;
        this.year = year;
        this.released = released;
        this.director = director;
    }

    /*
    * Getters
    * */
    public String getTitle()
    {
        return title;
    }

    public String getYear()
    {
        return year;
    }

    public String getReleased()
    {
        return released;
    }

    public String getDirector()
    {
        return director;
    }
}
