package com.example.popularmovies;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.popularmovies.businessobjects.MovieConstants;
import com.example.popularmovies.businessobjects.MovieInfo;
import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a list of Movie Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Created by Asim Qureshi.
 */
public class MoviesGridFragment extends Fragment implements MovieConstants, LoaderManager.LoaderCallbacks<Cursor> {

	public class FetchMoviesTask extends AsyncTask<String, Void, List<MovieInfo>> {

		final String TAG = FetchMoviesTask.class.getSimpleName();

		@Override
		protected List<MovieInfo> doInBackground(String... params) {
			String criteria = params[0];
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
				for (MovieInfo movieInfo : movieInfos) {
					ContentValues movieValues = new ContentValues();

					movieValues.put(MoviesContract.MoviesEntry._ID, movieInfo.getId());
					movieValues.put(MoviesContract.MoviesEntry.COLUMN_ADULT, 0);
					movieValues.put(MoviesContract.MoviesEntry.COLUMN_FAVORITE, 0);
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
				}
				int inserted = 0;
				// add to database
				if (cVVector.size() > 0) {
					ContentValues[] cvArray = new ContentValues[cVVector.size()];
					cVVector.toArray(cvArray);
					inserted = getActivity().getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
				}
				Log.d(TAG, "FetchMoviesTask Complete. " + inserted + " Inserted");

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
			return movieInfos;
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
		protected void onPostExecute(List<MovieInfo> movieInfos) {
			super.onPostExecute(movieInfos);
//			if (movieInfos != null) {
//				movieInfoAdapter.clear();
//				for (MovieInfo movieInfo : movieInfos) {
//					movieInfoAdapter.add(movieInfo);
//				}
//			}
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

//	public class MovieInfoAdapter extends ArrayAdapter<MovieInfo> {
//
//		public MovieInfoAdapter(Context context, List<MovieInfo> movieInfos) {
//			super(context, 0, movieInfos);
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// Get the data item for this position
//			final MovieInfo movieInfo = getItem(position);
//			// Check if an existing view is being reused, otherwise inflate the view
//			if (convertView == null) {
//				convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster_item, parent, false);
//			}
//			// Lookup view for data population
//			ImageView moviePosterImageView = (ImageView) convertView.findViewById(R.id.movie_poster);
//			TextView movieTitleView = (TextView) convertView.findViewById(R.id.movie_title);
//			// Populate the data into the template view using the data object
//			Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500/"
//					+ movieInfo.getPosterPath()
//					+ "?api_key="
//					+ BuildConfig.THE_MOVIE_DB_API_KEY
//					+ "&language=en&include_image_language=en,null").into(moviePosterImageView);
//			movieTitleView.setText(movieInfo.getTitle());
//			return convertView;
//		}
//	}

	// These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these must change.
	static final int COL_MOVIE_ID = 0;
	static final int COL_ADULT = 1;
	static final int COL_BACKDROP_PATH = 2;
	static final int COL_FAVORITE = 3;
	static final int COL_ORIGINAL_LANGUAGE = 4;
	static final int COL_ORIGINAL_TITLE = 5;
	static final int COL_OVERVIEW = 6;
	static final int COL_POPULARITY = 7;
	static final int COL_POSTER_PATH = 8;
	static final int COL_RELEASE_DATE = 9;
	static final int COL_TITLE = 10;
	static final int COL_VIDEO = 11;
	static final int COL_VOTE_AVERAGE = 12;
	static final int COL_VOTE_COUNT = 13;

	private static final String[] MOVIE_COLUMNS = {
			// In this case the id needs to be fully qualified with a table name, since
			// the content provider joins the location & weather tables in the background
			// (both have an _id column)
			// On the one hand, that's annoying.  On the other, you can search the weather table
			// using the location set by the user, which is only in the Location table.
			// So the convenience is worth it.
			MoviesContract.MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID,
			MoviesContract.MoviesEntry.COLUMN_ADULT,
			MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH,
			MoviesContract.MoviesEntry.COLUMN_FAVORITE,
			MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE,
			MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE,
			MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
			MoviesContract.MoviesEntry.COLUMN_POPULARITY,
			MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
			MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
			MoviesContract.MoviesEntry.COLUMN_TITLE,
			MoviesContract.MoviesEntry.COLUMN_VIDEO,
			MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
			MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT
	};
	private static final String SELECTED_KEY = "selected_position";

	public static MoviesGridFragment newInstance() {
		return new MoviesGridFragment();
	}
	private GridView gridView;
	private int mPosition = GridView.INVALID_POSITION;
	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private MovieInfoAdapter movieInfoAdapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MoviesGridFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created.  This
		// fragment only uses one loader, so we don't care about checking the id.
		// Sort order:  Ascending, by release date.
		String sortOrder = MoviesEntry.COLUMN_RELEASE_DATE + " ASC";

		return new CursorLoader(getActivity(),
				MoviesEntry.CONTENT_URI,
				null,
				null,
				null,
				sortOrder);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_main, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Create the movieInfoAdapter to convert the array to views
		movieInfoAdapter = new MovieInfoAdapter(getActivity(), null, 0);

		View view = inflater.inflate(R.layout.fragment_movieinfo, container, false);

// Attach the movieInfoAdapter to a ListView
		gridView = (GridView) view.findViewById(android.R.id.list);
		gridView.setAdapter(movieInfoAdapter);
		gridView.setClickable(true);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
				if (cursor != null) {
					Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
					Bundle mBundle = new Bundle();
					MovieInfo movieInfo = new MovieInfo();
					movieInfo.setPosterPath(cursor.getString(COL_POSTER_PATH));
					movieInfo.setAdult(cursor.getString(COL_ADULT));
					movieInfo.setOverview(cursor.getString(COL_OVERVIEW));
					movieInfo.setReleaseDate(cursor.getString(COL_RELEASE_DATE));
					movieInfo.setId(cursor.getInt(COL_MOVIE_ID));
					movieInfo.setOriginalTitle(cursor.getString(COL_ORIGINAL_TITLE));
					movieInfo.setOriginalLanguage(cursor.getString(COL_ORIGINAL_LANGUAGE));
					movieInfo.setTitle(cursor.getString(COL_TITLE));
					movieInfo.setBackdropPath(cursor.getString(COL_BACKDROP_PATH));
					movieInfo.setPopularity(cursor.getString(COL_POPULARITY));
					movieInfo.setVoteCount(cursor.getString(COL_VOTE_COUNT));
					movieInfo.setVideo(cursor.getString(COL_VIDEO));
					movieInfo.setVoteAverage(cursor.getString(COL_VOTE_AVERAGE));
					mBundle.putParcelable(MOVIE_INFO_PARCELABLE_KEY, movieInfo);
					movieDetailIntent.putExtras(mBundle);
					startActivity(movieDetailIntent);
				}
				mPosition = position;
			}
		});

		// If there's instance state, mine it for useful information.
		// The end-goal here is that the user never knows that turning their device sideways
		// does crazy lifecycle related things.  It should feel like some stuff stretched out,
		// or magically appeared to take advantage of room, but data or place in the app was never
		// actually *lost*.
		if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
			// The listview probably hasn't even been populated yet.  Actually perform the
			// swapout in onLoadFinished.
			mPosition = savedInstanceState.getInt(SELECTED_KEY);
		}

		return view;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		movieInfoAdapter.swapCursor(data);
		if (mPosition != ListView.INVALID_POSITION) {
			// If we don't need to restart the loader, and there's a desired position to restore
			// to, do so now.
			gridView.smoothScrollToPosition(mPosition);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		movieInfoAdapter.swapCursor(null);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// When tablets rotate, the currently selected list item needs to be saved.
		// When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
		// so check for that before storing.
		if (mPosition != ListView.INVALID_POSITION) {
			outState.putInt(SELECTED_KEY, mPosition);
		}
		super.onSaveInstanceState(outState);
	}


	private static final int MOVIE_LOADER = 0;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getLoaderManager().initLoader(MOVIE_LOADER, null, this);
		super.onActivityCreated(savedInstanceState);
	}


	protected void updateMovies() {
		FetchMoviesTask updateMoviesTask = new FetchMoviesTask();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String criteria = prefs.getString(getString(R.string.pref_search_criteria_key), getString(R.string.pref_search_criteria_default));
		updateMoviesTask.execute(criteria);
	}
}
