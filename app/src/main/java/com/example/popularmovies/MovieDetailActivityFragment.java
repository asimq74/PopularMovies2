package com.example.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.MovieTrailersFragment.MovieTrailerCallback;
import com.example.popularmovies.businessobjects.MovieConstants;
import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesContract.FavoritesEntry;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.squareup.picasso.Picasso;

/**
 * Placeholder fragment displaying Popular Movies Detail
 * <p/>
 * Created by Asim Qureshi.
 */
public class MovieDetailActivityFragment extends Fragment implements MovieConstants, LoaderManager.LoaderCallbacks<Cursor> {

	public static final int COL_ADULT = 1;
	public static final int COL_BACKDROP_PATH = 8;
	// These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
	// must change.
	public static final int COL_ID = 0;
	public static final int COL_ORIGINAL_LANGUAGE = 6;
	public static final int COL_ORIGINAL_TITLE = 5;
	public static final int COL_OVERVIEW = 3;
	public static final int COL_POPULARITY = 9;
	public static final int COL_POSTER_PATH = 2;
	public static final int COL_RELEASE_DATE = 4;
	public static final int COL_TITLE = 7;
	public static final int COL_VIDEO = 12;
	public static final int COL_VOTE_AVERAGE = 11;
	public static final int COL_VOTE_COUNT = 10;
	private static final int DETAIL_LOADER = 0;
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
	final String TAG = this.getClass().getSimpleName();
	private Uri mUri;
	private Button markAsFavoriteButton;
	private ImageView movieThumbnailView;
	private TextView movieTitleView;
	private TextView overviewView;
	private TextView ratingView;
	private TextView releaseDateView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getLoaderManager().initLoader(DETAIL_LOADER, null, this);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (null == mUri) {
			return null;
		}
		return new CursorLoader(
				getActivity(),
				mUri,
				MOVIE_COLUMNS,
				null,
				null,
				null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mUri = getActivity().getIntent().getData();
		View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
		movieTitleView = (TextView) rootView.findViewById(R.id.movie_title);
		movieThumbnailView = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
		overviewView = (TextView) rootView.findViewById(R.id.overview);
		ratingView = (TextView) rootView.findViewById(R.id.rating);
		releaseDateView = (TextView) rootView.findViewById(R.id.release_date);
		markAsFavoriteButton = (Button) rootView.findViewById(R.id.markAsFavoriteButton);
		return rootView;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data != null && data.moveToFirst()) {
			movieTitleView.setText(data.getString(COL_TITLE));
			Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500/"
					+ data.getString(COL_POSTER_PATH)
					+ "?api_key="
					+ BuildConfig.THE_MOVIE_DB_API_KEY
					+ "&language=en&include_image_language=en,null").into(movieThumbnailView);
			overviewView.setText(data.getString(COL_OVERVIEW));
			ratingView.setText(String.format("%s/10", data.getFloat(COL_VOTE_AVERAGE)));
			releaseDateView.setText(Utility.getYear(data.getString(COL_RELEASE_DATE)));
			final String lastPathSegment = mUri.getLastPathSegment();
			final long movieId = Long.parseLong(lastPathSegment);
			Cursor isFavoriteCursor = getContext().getContentResolver().query(FavoritesEntry.buildFavoritesById(movieId), null, null, null, null);
			if (isFavoriteCursor != null && isFavoriteCursor.moveToFirst()) {
				setButtonToFavorite(markAsFavoriteButton);
			} else {
				setButtonToMarkAsFavorite(markAsFavoriteButton);
			}
			markAsFavoriteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Button button = (Button) view;
					final ContentValues favoritesValues = Utility.createFavoritesValues(lastPathSegment);
					if (button.getText().equals(getString(R.string.mark_as_favorite))) {
						setButtonToFavorite(button);
						Uri returnUri = getContext().getContentResolver().insert(FavoritesEntry.buildFavoritesById(movieId), favoritesValues);
						Log.d(TAG, String.format("Insert into favorites Complete. %s %s", favoritesValues, returnUri));
					} else {
						setButtonToMarkAsFavorite(button);
						int rowsDeleted = getContext().getContentResolver().delete(FavoritesEntry.removeFavoriteById(movieId), null, null);
						Log.d(TAG, "deleted " + rowsDeleted + " row from " + FavoritesEntry.removeFavoriteById(movieId));
					}
				}
			});
			// If onCreateOptionsMenu has already happened, we need to update the share intent now.
			if (mShareActionProvider != null) {
				mShareActionProvider.setShareIntent(createShareFirstTrailerIntent());
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.menu_movie_detail, menu);

		// Retrieve the share menu item
		MenuItem menuItem = menu.findItem(R.id.action_share);

		// Get the provider and hold onto it to set/change the share intent.
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

		// If onLoadFinished happens before this, we can go ahead and set the share intent now.
		if (((MovieTrailerCallback)getActivity()).getFirstTrailerUrl() != null) {
			mShareActionProvider.setShareIntent(createShareFirstTrailerIntent());
		}
	}

	private ShareActionProvider mShareActionProvider;

	public Intent createShareFirstTrailerIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, ((MovieTrailerCallback)getActivity()).getFirstTrailerUrl() + FORECAST_SHARE_HASHTAG);
		return shareIntent;
	}

	private static final String FORECAST_SHARE_HASHTAG = " #PopularMoviesApp";

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	private void setButtonToFavorite(Button button) {
		updateButton(button, R.color.colorSkyBlue, R.string.favorite);
	}

	private void setButtonToMarkAsFavorite(Button button) {
		updateButton(button, R.color.colorMovieDetailHeader, R.string.mark_as_favorite);
	}

	private void updateButton(Button button, int color, int buttonText) {
		button.setBackgroundColor(ContextCompat.getColor(getActivity(), color));
		button.setText(getString(buttonText));
	}

}
