package com.example.popularmovies.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.R;
import com.example.popularmovies.Utility;
import com.example.popularmovies.businessobjects.MovieInfo;
import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesContract.HighestRatedEntry;
import com.example.popularmovies.data.MoviesContract.MostPopularEntry;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by U1C306 on 2/2/2017.
 */

public class FetchMoviesService extends IntentService {

	public static final String SEARCH_CRITERIA_EXTRA = "sce";
	private final String TAG = FetchMoviesService.class.getSimpleName();

	public FetchMoviesService() {
		super("FetchMovies");
	}

	protected List<MovieInfo> formatJson(String json) throws JSONException {
		// These are the names of the JSON objects that need to be extracted.
		final String RESULTS = "results";
		final String POSTER_PATH = "poster_path";
		final String ADULT = "adult";
		final String OVERVIEW = "overview";
		final String RELEASE_DATE = "release_date";
		final String GENRE_IDS = "genre_ids";
		final String ID = "id";
		final String ORIGINAL_TITLE = "original_title";
		final String ORIGINAL_LANGUAGE = "original_language";
		final String TITLE = "title";
		final String BACKDROP_PATH = "backdrop_path";
		final String POPULARITY = "popularity";
		final String VOTE_COUNT = "vote_count";
		final String VIDEO = "video";
		final String VOTE_AVERAGE = "vote_average";

		JSONObject forecastJson = new JSONObject(json);
		JSONArray moviesArray = forecastJson.getJSONArray(RESULTS);
		List<MovieInfo> movies = new ArrayList<>();
		for (int i = 0; i < moviesArray.length(); i++) {
			JSONObject movieInfoJsonObject = moviesArray.getJSONObject(i);
			MovieInfo movieInfo = new MovieInfo();
			movieInfo.setPosterPath(movieInfoJsonObject.getString(POSTER_PATH));
			movieInfo.setAdult(movieInfoJsonObject.getString(ADULT));
			movieInfo.setOverview(movieInfoJsonObject.getString(OVERVIEW));
			movieInfo.setReleaseDate(movieInfoJsonObject.getString(RELEASE_DATE));
			movieInfo.setId(movieInfoJsonObject.getInt(ID));
			movieInfo.setOriginalTitle(movieInfoJsonObject.getString(ORIGINAL_TITLE));
			movieInfo.setOriginalLanguage(movieInfoJsonObject.getString(ORIGINAL_LANGUAGE));
			movieInfo.setTitle(movieInfoJsonObject.getString(TITLE));
			movieInfo.setBackdropPath(movieInfoJsonObject.getString(BACKDROP_PATH));
			movieInfo.setPopularity(movieInfoJsonObject.getString(POPULARITY));
			movieInfo.setVoteCount(movieInfoJsonObject.getString(VOTE_COUNT));
			movieInfo.setVideo(movieInfoJsonObject.getString(VIDEO));
			movieInfo.setVoteAverage(movieInfoJsonObject.getString(VOTE_AVERAGE));
			populateGenreIds(GENRE_IDS, movieInfoJsonObject, movieInfo);
			movies.add(movieInfo);
		}
		return movies;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String criteria = intent.getStringExtra(SEARCH_CRITERIA_EXTRA);
		List<MovieInfo> movieInfos = new ArrayList<>();
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String moviesJsonString;

		try {
			Uri.Builder builder = new Uri.Builder();
			builder.scheme("http")
					.authority("api.themoviedb.org")
					.appendPath("3")
					.appendPath("movie")
					.appendPath(criteria)
					.appendQueryParameter("sort_by", "popularity.desc")
					.appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);

			final String builtURLString = builder.build().toString();
			Log.i(TAG, String.format("builtURLString: %s", builtURLString));
			URL url = new URL(builtURLString);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			// Read the input stream into a String
			InputStream inputStream = urlConnection.getInputStream();
			StringBuilder buffer = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			if (buffer.length() == 0) {
				Log.d(TAG, "Stream was empty");
			}
			moviesJsonString = buffer.toString();
			movieInfos = formatJson(moviesJsonString);
			Vector<ContentValues> cVVector = new Vector<ContentValues>(movieInfos.size());
			Vector<ContentValues> referenceVector = new Vector<ContentValues>(movieInfos.size());
			List<String> ids = new ArrayList<>();
			for (MovieInfo movieInfo : movieInfos) {
				ContentValues movieValues = new ContentValues();
				ContentValues movieReferenceValues = new ContentValues();
				ids.add(movieInfo.getId() + "");
				movieReferenceValues.put(MostPopularEntry.COLUMN_MOVIE_ID, movieInfo.getId());
				movieValues.put(MoviesContract.MoviesEntry._ID, movieInfo.getId());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_ADULT, 0);
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, movieInfo.getBackdropPath());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE, movieInfo.getOriginalLanguage());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, movieInfo.getOriginalTitle());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, movieInfo.getOverview());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, movieInfo.getPopularity());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movieInfo.getPosterPath());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movieInfo.getReleaseDate());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movieInfo.getTitle());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_VIDEO, 0);
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movieInfo.getVoteAverage());
				movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, movieInfo.getVoteCount());

				cVVector.add(movieValues);
				referenceVector.add(movieReferenceValues);
			}

			int rowsDeleted = getApplicationContext().getContentResolver().delete(MoviesEntry.CONTENT_URI, Utility.createMovieIdSelection(ids), ids.toArray(new String[ids.size()]));
			Log.d(TAG, "FetchMoviesTask before update. " + rowsDeleted + " rows deleted from " + MoviesEntry.TABLE_NAME );
			int referenceRowsDeleted = getApplicationContext().getContentResolver().delete(Utility.determineDeleteUriBasedOnCriteria(criteria), null, null);
			Log.d(TAG, "FetchMoviesTask before update. " + referenceRowsDeleted + " rows deleted from " + Utility.determineTableBasedOnCriteria(criteria) );

			int inserted = 0;
			int referenceRecordsInserted = 0;
			// add to database
			if (cVVector.size() > 0) {
				ContentValues[] cvArray = new ContentValues[cVVector.size()];
				cVVector.toArray(cvArray);
				ContentValues[] referenceArray = new ContentValues[referenceVector.size()];
				referenceVector.toArray(referenceArray);
				inserted = getApplicationContext().getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
				referenceRecordsInserted = getApplicationContext().getContentResolver().bulkInsert(Utility.determineContentUriBasedOnCriteria(criteria), referenceArray);
			}
			Log.d(TAG, "FetchMoviesTask Complete. " + inserted + " inserted into " + MoviesEntry.TABLE_NAME);
			Log.d(TAG, "FetchMoviesTask Complete. " + inserted + " inserted into " +  Utility.determineTableBasedOnCriteria(criteria));

		} catch (JSONException e) {
			Log.e(TAG, String.format("JSONException Error e: %s", e.getMessage()), e);

		} catch (IOException e) {
			Log.e(TAG, String.format("IOException Error e: %s", e.getMessage()), e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					Log.e(TAG, "Error closing stream", e);
				}
			}
		}
		return;
	}


	protected void populateGenreIds(String GENRE_IDS, JSONObject movieInfoJsonObject, MovieInfo movieInfo) throws JSONException {
		JSONArray jsonArray = movieInfoJsonObject.getJSONArray(GENRE_IDS);
		if (jsonArray != null) {
			for (int j = 0; j < jsonArray.length(); j++) {
				movieInfo.getGenreIds().add(Integer.valueOf(jsonArray.get(j).toString()));
			}
		}
	}
}

