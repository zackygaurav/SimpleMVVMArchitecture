package in.itechvalley.topmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.itechvalley.topmovies.R;
import in.itechvalley.topmovies.model.MovieModel;
import in.itechvalley.topmovies.model.TopMoviesModel;

public class TopMoviesRecyclerAdapter extends RecyclerView.Adapter<TopMoviesRecyclerAdapter.TopMoviesViewHolder>
{
    /*
    * Global Instance of Interface
    * */
    private TopMoviesAdapterListener listener;

    /*
    * Global Instance of List to store all the movies
    * */
    private List<MovieModel> topMoviesModelList;

    /*
    * Constructor
    * */
    public TopMoviesRecyclerAdapter(List<MovieModel> topMoviesModelList, TopMoviesAdapterListener listener)
    {
        this.topMoviesModelList = topMoviesModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new TopMoviesViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TopMoviesViewHolder holder, int position)
    {
        MovieModel topMoviesModel = topMoviesModelList.get(position);

        /*
        * Update Movie Title
        * */
        holder.txtMovieTitle.setText(topMoviesModel.getTitle());

        /*
        * Click Listener on Item
        * */
        holder.itemView.setOnClickListener(v ->
        {
            listener.onItemClick(topMoviesModel.getTitle());
        });
    }

    @Override
    public int getItemCount()
    {
        return topMoviesModelList.size();
    }

    public void refreshRecyclerView(List<MovieModel> newMoviesList)
    {
        if (this.topMoviesModelList != null)
        {
            this.topMoviesModelList.clear();
            this.topMoviesModelList.addAll(newMoviesList);
        }
        else
        {
            this.topMoviesModelList = newMoviesList;
        }

        notifyDataSetChanged();
    }

    /*
    * ViewHolder
    * */
    class TopMoviesViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(android.R.id.text1)
        TextView txtMovieTitle;

        /*
        * Constructor
        * */
        public TopMoviesViewHolder(@NonNull View itemView)
        {
            super(itemView);

            /*
            * Init ButterKnife
            * */
            ButterKnife.bind(this, itemView);
        }
    }

    /*
    * Interface for Callbacks to MainActivity
    * */
    public interface TopMoviesAdapterListener
    {
        void onItemClick(String movieTitle);
    }
}
