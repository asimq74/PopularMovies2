package com.example.popularmovies.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.popularmovies.businessobjects.MovieInfo;

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
        RetrieveMovieInfoApi<MovieInfo> retrieveMoviesHandler = new RetrieveMovies(getApplicationContext());
        retrieveMoviesHandler.retrieveMovieInformation(criteria);
    }

}

