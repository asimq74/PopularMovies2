package com.example.popularmovies.service;

import android.net.Uri;

import org.json.JSONException;

import java.util.List;

/**
 * Created by asimqureshi on 3/14/17.
 */

public interface RetrieveMovieInfoApi<I, O> {

    Uri.Builder createUriBuilder(String criteria);

    List<I> formatJson(String json) throws JSONException;

    O retrieveMovieInformation(String criteria);
}
