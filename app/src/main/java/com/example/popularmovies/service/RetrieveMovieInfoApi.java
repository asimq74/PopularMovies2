package com.example.popularmovies.service;

import java.util.List;

import android.net.Uri;
import android.support.annotation.NonNull;

import org.json.JSONException;

/**
 * API containing methods associated with retrieving information about movies
 *
 * @author Asim Qureshi
 *
 */
public interface RetrieveMovieInfoApi<I, O> {

	/**
	 * Create a Uri builder for the themoviedb.org API calls
	 * @param criteria
	 * @return Uri.Builder
	 */
	@NonNull
	Uri.Builder createUriBuilder(@NonNull String criteria);

	/**
	 * format a json string
	 * @param json
	 * @return List
	 */
	@NonNull
	List<I> formatJson(@NonNull String json) throws JSONException;

	/**
	 * retrieve movie related information from themoviedb.org API calls
	 * @param criteria
	 * @return O
	 */
	@NonNull
	O retrieveMovieInformation(@NonNull String criteria);
}
