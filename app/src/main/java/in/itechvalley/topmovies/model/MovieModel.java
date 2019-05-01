package in.itechvalley.topmovies.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "table_movies", indices = {@Index(value = "title", unique = true), @Index(value = "year", unique = true)})
public class MovieModel implements Serializable
{
    @ColumnInfo(name = "title")
    @SerializedName("Title")
    private String title;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "year")
    @SerializedName("Year")
    private String year;

    @ColumnInfo(name = "released")
    @SerializedName("Released")
    private String released;

    // @Ignore
    @ColumnInfo(name = "director")
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
