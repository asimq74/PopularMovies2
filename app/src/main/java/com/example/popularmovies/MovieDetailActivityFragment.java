package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.businessobjects.MovieConstants;
import com.example.popularmovies.businessobjects.MovieInfo;
import com.example.popularmovies.task.ListMovieImagesAsyncTask;
import com.squareup.picasso.Picasso;

/**
 * Placeholder fragment displaying Popular Movies Detail
 * <p/>
 * Created by Asim Qureshi.
 */
public class MovieDetailActivityFragment extends Fragment implements MovieConstants {

    private MovieInfo movieInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(MOVIE_INFO_PARCELABLE_KEY)) {
            movieInfo = intent.getParcelableExtra(MOVIE_INFO_PARCELABLE_KEY);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        final ImageView moviePosterView = (ImageView) getView().findViewById(R.id.movie_poster);
        ListMovieImagesAsyncTask listMoviesTask = new ListMovieImagesAsyncTask(getActivity(), moviePosterView);
        listMoviesTask.execute(movieInfo.getId());
        final TextView movieTitleView = (TextView) getView().findViewById(R.id.movie_title);
        movieTitleView.setText(movieInfo.getTitle());
        final ImageView movieThumbnailView = (ImageView) getView().findViewById(R.id.movie_thumbnail);
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w154/"
                + movieInfo.getPosterPath()
                + "?api_key="
                + BuildConfig.THE_MOVIE_DB_API_KEY
                + "&language=en&include_image_language=en,null").into(movieThumbnailView);
        final TextView overviewView = (TextView) getView().findViewById(R.id.overview);
        overviewView.setText(movieInfo.getOverview());
        final TextView ratingView = (TextView) getView().findViewById(R.id.rating);
        ratingView.setText(movieInfo.getVoteAverage());
        final TextView releaseDateView = (TextView) getView().findViewById(R.id.release_date);
        releaseDateView.setText(movieInfo.getReleaseDateLongFormat());
    }

}
