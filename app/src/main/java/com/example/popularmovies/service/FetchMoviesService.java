package com.example.popularmovies.service;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;

import com.example.popularmovies.businessobjects.MovieInfo;
import com.example.popularmovies.data.MoviesContract;

/**
 * Created by U1C306 on 2/2/2017.
 */

public class FetchMoviesService extends IntentService {

    public static final String SEARCH_CRITERIA_EXTRA = "sce";

    public FetchMoviesService() {
        super("FetchMovies");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String criteria = intent.getStringExtra(SEARCH_CRITERIA_EXTRA);
        if (MoviesContract.FAVORITES.equals(criteria)) {
            return;
        }
        RetrieveMovieInfoApi<MovieInfo, List<String>> retrieveMoviesHandler = new MovieInfoRetriever(getApplicationContext());
        List<String> movieIds = retrieveMoviesHandler.retrieveMovieInformation(criteria);
        MovieReviewsRetriever reviewsHandler = new MovieReviewsRetriever(getApplicationContext());
        MovieVideosRetriever videosHandler = new MovieVideosRetriever(getApplicationContext());
        for (String movieId : movieIds) {
            List<String> reviewIds = reviewsHandler.retrieveMovieInformation(movieId);
            List<String> videoIds = videosHandler.retrieveMovieInformation(movieId);
        }
    }

}

