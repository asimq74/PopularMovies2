package com.example.popularmovies.service;

import android.net.Uri;

import org.json.JSONException;

import java.util.List;

/**
 * Created by asimqureshi on 3/14/17.
 */

public abstract class BaseRetrieveMovieInfoApi<I, O> implements RetrieveMovieInfoApi<I, O> {
    @Override
    public abstract Uri.Builder createUriBuilder(String criteria);

    @Override
    public abstract List<I> formatJson(String json) throws JSONException;
}
