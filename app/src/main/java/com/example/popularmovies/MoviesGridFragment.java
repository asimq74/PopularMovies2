package com.example.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesProvider;
import com.example.popularmovies.sync.MoviesSyncAdapter;

/**
 * A fragment representing a list of Movie Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Created by Asim Qureshi.
 */
public class MoviesGridFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface MovieDetailSelectedItemCallback {

		/**
		 * DetailFragmentCallback for when an item has been selected.
		 */
		void onItemSelected(Uri detailUri);
	}

	static final int COL_MOVIE_ID = 0;
	static final int COL_POSTER_PATH = 2;
	private static final String[] MOVIE_COLUMNS = {
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
			MoviesContract.MoviesEntry.COLUMN_VIDEO,
			MoviesContract.MoviesEntry.COLUMN_SEARCH_CRITERIA
	};
	private static final int MOVIE_LOADER = 0;
	private static final String SELECTED_KEY = "selected_position";

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
		final Uri movieInfoUri = Utility.getMovieInfoUri(getActivity());
		Log.i(getClass().getSimpleName(), String.format("preferred criteria: %s", movieInfoUri.toString()));
		return new CursorLoader(getActivity(),
				movieInfoUri,
				MOVIE_COLUMNS,
				MoviesProvider.moviesSearchCriteriaSelection,
				new String[]{Utility.getPreferredCriteria(getActivity())},
				Utility.getPreferredCriteria(getActivity()).equals(MoviesContract.TOP_RATED) ? MoviesProvider.ratingSortOrder : MoviesProvider.popularitySortOrder);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		if (Utility.isNetworkAvailable(getActivity())) {
			inflater.inflate(R.menu.menu_main, menu);
		}
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
					((MovieDetailSelectedItemCallback) getActivity()).onItemSelected(MoviesContract.MoviesEntry.buildMovieById(cursor.getInt(COL_MOVIE_ID)));
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
		MoviesSyncAdapter.syncImmediately(getActivity());
	}
}
