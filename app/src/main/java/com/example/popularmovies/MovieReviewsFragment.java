package com.example.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.popularmovies.data.MoviesContract.ReviewsEntry;

/**
 * A fragment representing a list of Items.
 * <p />
 */
public class MovieReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	final String TAG = this.getClass().getSimpleName();
	private ListView mListView;
	private int mPosition = ListView.INVALID_POSITION;
	private Uri mUri;
	private ReviewsAdapter reviewsAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUri = getActivity().getIntent().getData();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		if (null == mUri) {
			return null;
		}
		final String lastPathSegment = mUri.getLastPathSegment();
		final long movieId = Long.parseLong(lastPathSegment);
		final Uri buildReviewsByIdUri = ReviewsEntry.buildReviewsById(movieId);
		return new CursorLoader(getActivity(),
				buildReviewsByIdUri,
				null,
				null,
				null,
				null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		reviewsAdapter = new ReviewsAdapter(getActivity(), null, 0);

		View view = inflater.inflate(R.layout.fragment_review_list, container, false);
		// Get a reference to the ListView, and attach this adapter to it.
		mListView = (ListView) view.findViewById(R.id.reviewsList);
		mListView.setAdapter(reviewsAdapter);

		return view;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		reviewsAdapter.swapCursor(data);
		if (mPosition != ListView.INVALID_POSITION) {
			// If we don't need to restart the loader, and there's a desired position to restore
			// to, do so now.
			mListView.smoothScrollToPosition(mPosition);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		reviewsAdapter.swapCursor(null);
	}

}
