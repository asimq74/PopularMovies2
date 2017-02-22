package com.example.popularmovies;

import java.net.URI;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.businessobjects.MovieConstants;
import com.example.popularmovies.businessobjects.MovieInfo;
import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.task.ListMovieImagesAsyncTask;
import com.squareup.picasso.Picasso;

/**
 * Placeholder fragment displaying Popular Movies Detail
 * <p/>
 * Created by Asim Qureshi.
 */
public class MovieDetailActivityFragment extends Fragment implements MovieConstants, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] MOVIE_COLUMNS = {
        // In this case the id needs to be fully qualified with a table name, since
        // the content provider joins the location & weather tables in the background
        // (both have an _id column)
        // On the one hand, that's annoying.  On the other, you can search the weather table
        // using the location set by the user, which is only in the Location table.
        // So the convenience is worth it.
        MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID,
        MoviesContract.MoviesEntry.COLUMN_ADULT,
        MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
        MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
        MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
        MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
        MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE,
        MoviesContract.MoviesEntry.COLUMN_TITLE,
        MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,
        MoviesContract.MoviesEntry.COLUMN_POPULARITY,
        MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT,
        MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
        MoviesContract.MoviesEntry.COLUMN_VIDEO
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_ADULT = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_OVERVIEW = 3;
    public static final int COL_RELEASE_DATE = 4;
    public static final int COL_ORIGINAL_TITLE= 5;
    public static final int COL_ORIGINAL_LANGUAGE = 6;
    public static final int COL_TITLE = 7;
    public static final int COL_BACKDROP_PATH = 8;
    public static final int COL_POPULARITY = 9;
    public static final int COL_VOTE_COUNT = 10;
    public static final int COL_VOTE_AVERAGE = 11;
    public static final int COL_VIDEO = 12;

    private ImageView moviePosterView;
    private ImageView movieThumbnailView;
    private TextView movieTitleView;
    private TextView overviewView;
    private TextView ratingView;
    private TextView releaseDateView;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null == mUri ) {
            return null;
        }
        return new CursorLoader(
            getActivity(),
            mUri,
            MOVIE_COLUMNS,
            null,
            new String[]{},
            null);
    }


    private static final int DETAIL_LOADER = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            ListMovieImagesAsyncTask listMoviesTask = new ListMovieImagesAsyncTask(getActivity(), moviePosterView);
            listMoviesTask.execute(data.getInt(COL_ID));
            movieTitleView.setText(data.getString(COL_TITLE));
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w154/"
                + data.getString(COL_POSTER_PATH)
                + "?api_key="
                + BuildConfig.THE_MOVIE_DB_API_KEY
                + "&language=en&include_image_language=en,null").into(movieThumbnailView);
            overviewView.setText(data.getString(COL_OVERVIEW));
            ratingView.setText(String.format("%s",data.getFloat(COL_VOTE_AVERAGE)));
            releaseDateView.setText(data.getString(COL_RELEASE_DATE));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private int movieId = 0;
    private Uri mUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUri = getActivity().getIntent().getData();
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        moviePosterView = (ImageView) rootView.findViewById(R.id.movie_poster);
        movieTitleView = (TextView) rootView.findViewById(R.id.movie_title);
        movieThumbnailView = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
        overviewView = (TextView) rootView.findViewById(R.id.overview);
        ratingView = (TextView) rootView.findViewById(R.id.rating);
        releaseDateView = (TextView) rootView.findViewById(R.id.release_date);
        return rootView;
    }


}
