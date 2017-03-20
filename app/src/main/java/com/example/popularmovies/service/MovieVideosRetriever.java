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
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.businessobjects.Video;
import com.example.popularmovies.businessobjects.Video;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by U1C306 on 3/17/2017.
 */

public class MovieVideosRetriever implements RetrieveMovieInfoApi<Video, List<String>> {

	@Override
	public Builder createUriBuilder(String criteria) {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http")
				.authority("api.themoviedb.org")
				.appendPath("3")
				.appendPath("movie")
				.appendPath(criteria)
				.appendPath("videos")
				.appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);
		return builder;
	}


	private final Context context;
	final String TAG = this.getClass().getSimpleName();

	public MovieVideosRetriever(Context context) {
		this.context = context;
	}

	@Override
	public List<Video> formatJson(String json) throws JSONException {
		// These are the names of the JSON objects that need to be extracted.
		final String RESULTS = "results";
		final String KEY = "key";
		final String NAME = "name";
		final String ID = "id";
		final String SITE = "site";
		final String SIZE = "size";
		final String TYPE = "type";
		JSONObject forecastJson = new JSONObject(json);
		int movieId = forecastJson.getInt(ID);
		JSONArray videosArray = forecastJson.getJSONArray(RESULTS);
		List<Video> videos = new ArrayList<>();
		for (int i = 0; i < videosArray.length(); i++) {
			JSONObject movieInfoJsonObject = videosArray.getJSONObject(i);
			Video video = new Video();
			video.setKey(movieInfoJsonObject.getString(KEY));
			video.setName(movieInfoJsonObject.getString(NAME));
			video.setMovieId(movieId);
			video.setVideoId(movieInfoJsonObject.getString(ID));
			video.setType(movieInfoJsonObject.getString(TYPE));
			video.setSite(movieInfoJsonObject.getString(SITE));
			video.setSize(movieInfoJsonObject.getInt(SIZE));
			videos.add(video);
		}
		return videos;
	}

	@Override
	public List<String> retrieveMovieInformation(String criteria) {
		List<Video> videos = new ArrayList<>();
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;
		String videosJsonString;
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
			videosJsonString = buffer.toString();
			Log.i(TAG, String.format("videosJsonString: %s", videosJsonString));
			videos = formatJson(videosJsonString);
			Vector<ContentValues> cVVector = new Vector<>(videos.size());
			for (Video video : videos) {
				ContentValues videoValues = new ContentValues();
				videoValues.put(VideosEntry.COLUMN_MOVIE_ID, video.getMovieId());
				videoValues.put(VideosEntry.COLUMN_SITE, video.getSite());
				videoValues.put(VideosEntry.COLUMN_TYPE, video.getType());
				videoValues.put(VideosEntry.COLUMN_SIZE, video.getSize());
				videoValues.put(VideosEntry.COLUMN_KEY, video.getKey());
				videoValues.put(VideosEntry.COLUMN_NAME, video.getName());
				videoValues.put(VideosEntry._ID, video.getVideoId());
				ids.add(video.getVideoId());
				cVVector.add(videoValues);
			}

			int inserted = 0;
			// add to database
			if (cVVector.size() > 0) {
				ContentValues[] cvArray = new ContentValues[cVVector.size()];
				cVVector.toArray(cvArray);
				inserted = context.getContentResolver().bulkInsert(VideosEntry.CONTENT_URI, cvArray);
			}
			Log.d(TAG, String.format("%s Complete. %s inserted into %s", TAG, inserted, VideosEntry.TABLE_NAME));

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
