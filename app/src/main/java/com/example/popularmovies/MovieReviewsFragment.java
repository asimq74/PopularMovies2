package com.example.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.popularmovies.ReviewsAdapter.ViewHolder;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;

/**
 * A fragment representing a list of Items.
 * <p />
 */
public class MovieReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final int REVIEWS_LOADER = 0;
	final String TAG = this.getClass().getSimpleName();
	private Uri mUri;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getLoaderManager().initLoader(REVIEWS_LOADER, null, this);
		super.onActivityCreated(savedInstanceState);
	}

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
		View view = inflater.inflate(R.layout.fragment_review_layout, container, false);
		return view;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		public final TextView authorView;
		public final TextView contentView;
		public final TextView urlView;

		public ViewHolder(View view) {
			super(view);
			authorView = (TextView) view.findViewById(R.id.author);
			contentView = (TextView) view.findViewById(R.id.content);
			urlView = (TextView) view.findViewById(R.id.url);
		}

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		TextView authorView;
		TextView contentView;
		TextView urlView;
		data.moveToFirst();
		final LinearLayout reviewsLayout = (LinearLayout) getView();
		reviewsLayout.removeAllViews();
		if (data == null && !data.moveToFirst()) {
			return;
		}
		Log.d(TAG, String.format("review count: %s for movie_id: %s", data.getCount(), mUri.getLastPathSegment()));
		if (data.moveToFirst()) {
			do {
				View reviewDataView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_review, reviewsLayout, false);
				authorView = (TextView) reviewDataView.findViewById(R.id.author);
				contentView = (TextView) reviewDataView.findViewById(R.id.content);
				urlView = (TextView) reviewDataView.findViewById(R.id.url);
				Log.i(TAG, String.format("content %s", data.getString(data.getColumnIndex(ReviewsEntry.COLUMN_CONTENT))));
				contentView.setText(data.getString(data.getColumnIndex(ReviewsEntry.COLUMN_CONTENT)));
				authorView.setText(data.getString(data.getColumnIndex(ReviewsEntry.COLUMN_AUTHOR)));
				urlView.setText(data.getString(data.getColumnIndex(ReviewsEntry.COLUMN_URL)));
				reviewsLayout.addView(reviewDataView);
			} while (data.moveToNext());
		}
		data.close();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

}
