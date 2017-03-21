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

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.businessobjects.Review;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementation of RetrieveMovieInfoApi that retrieves and populates movie reviews for a specific movie
 *
 * @author Asim Qureshi
 *
 */
public class MovieReviewsRetriever implements RetrieveMovieInfoApi<Review, List<String>> {

	private final String TAG = this.getClass().getSimpleName();
	private final Context context;

	public MovieReviewsRetriever(Context context) {
		this.context = context;
	}

	@Override
	public Builder createUriBuilder(String criteria) {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http")
				.authority("api.themoviedb.org")
				.appendPath("3")
				.appendPath("movie")
				.appendPath(criteria)
				.appendPath("reviews")
				.appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);
		return builder;
	}

	@Override
	public List<Review> formatJson(String json) throws JSONException {
		// These are the names of the JSON objects that need to be extracted.
		final String RESULTS = "results";
		final String AUTHOR = "author";
		final String CONTENT = "content";
		final String ID = "id";
		final String URL = "url";

		JSONObject forecastJson = new JSONObject(json);
		int movieId = forecastJson.getInt(ID);
		JSONArray reviewsArray = forecastJson.getJSONArray(RESULTS);
		List<Review> reviews = new ArrayList<>();
		for (int i = 0; i < reviewsArray.length(); i++) {
			JSONObject movieInfoJsonObject = reviewsArray.getJSONObject(i);
			Review review = new Review();
			review.setAuthor(movieInfoJsonObject.getString(AUTHOR));
			review.setContent(movieInfoJsonObject.getString(CONTENT));
			review.setMovieId(movieId);
			review.setReviewId(movieInfoJsonObject.getString(ID));
			review.setUrl(movieInfoJsonObject.getString(URL));
			reviews.add(review);
		}
		return reviews;
	}

	@Override
	public List<String> retrieveMovieInformation(String criteria) {
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String reviewsJsonString;
		List<String> ids = new ArrayList<>();

		try {
			Uri.Builder builder = createUriBuilder(criteria);

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
			reviewsJsonString = buffer.toString();
			Log.i(TAG, String.format("reviewsJsonString: %s", reviewsJsonString));
			List<Review> reviews = formatJson(reviewsJsonString);
			Vector<ContentValues> cVVector = new Vector<>(reviews.size());
			for (Review review : reviews) {
				ContentValues reviewValues = new ContentValues();
				reviewValues.put(ReviewsEntry.COLUMN_MOVIE_ID, review.getMovieId());
				reviewValues.put(ReviewsEntry.COLUMN_CONTENT, review.getContent());
				reviewValues.put(ReviewsEntry.COLUMN_AUTHOR, review.getAuthor());
				reviewValues.put(ReviewsEntry.COLUMN_URL, review.getUrl());
				reviewValues.put(ReviewsEntry._ID, review.getReviewId());
				ids.add(review.getReviewId());
				cVVector.add(reviewValues);
			}

			int inserted = 0;
			// add to database
			if (cVVector.size() > 0) {
				ContentValues[] cvArray = new ContentValues[cVVector.size()];
				cVVector.toArray(cvArray);
				inserted = context.getContentResolver().bulkInsert(ReviewsEntry.CONTENT_URI, cvArray);
			}
			Log.d(TAG, String.format("%s Complete. %s inserted into %s", TAG, inserted, ReviewsEntry.TABLE_NAME));

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
		return ids;
	}
}
