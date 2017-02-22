package com.example.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.popularmovies.service.FetchMoviesService;

/**
 * A fragment representing a list of Movie Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Created by Asim Qureshi.
 */
public class MoviesGridFragment extends Fragment implements MovieConstants, LoaderManager.LoaderCallbacks<Cursor> {

	static final int COL_ADULT = 1;
	static final int COL_BACKDROP_PATH = 8;
	static final int COL_FAVORITE = 12;
	// These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these must change.
	static final int COL_MOVIE_ID = 0;
	static final int COL_ORIGINAL_LANGUAGE = 6;
	static final int COL_ORIGINAL_TITLE = 5;
	static final int COL_OVERVIEW = 3;
	static final int COL_POPULARITY = 9;
	static final int COL_POSTER_PATH = 2;
	static final int COL_RELEASE_DATE = 4;
	static final int COL_TITLE = 7;
	static final int COL_VIDEO = 13;
	static final int COL_VOTE_AVERAGE = 11;
	static final int COL_VOTE_COUNT = 10;

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
	private static final int MOVIE_LOADER = 0;
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
	public void onActivityCreated(Bundle savedInstanceState) {
		getLoaderManager().initLoader(MOVIE_LOADER, null, this);
		super.onActivityCreated(savedInstanceState);
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
		String sortOrder = MoviesEntry.COLUMN_RELEASE_DATE + " DESC";

		return new CursorLoader(getActivity(),
				MoviesEntry.CONTENT_URI,
				MOVIE_COLUMNS,
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

	protected void onMoviesSortCriteriaChanged() {
		updateMovies();
		getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
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

	protected void updateMovies() {
		String criteria = Utility.getPreferredCriteria(getActivity());
		Intent intent = new Intent(getActivity(), FetchMoviesService.class);
		intent.putExtra(FetchMoviesService.SEARCH_CRITERIA_EXTRA, criteria);
		getActivity().startService(intent);
	}
}
