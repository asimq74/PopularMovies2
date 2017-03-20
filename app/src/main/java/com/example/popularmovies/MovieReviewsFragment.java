package com.example.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.popularmovies.data.MoviesContract.ReviewsEntry;

/**
 * A fragment representing a list of Items.
 * <p />
 */
public class MovieReviewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public static class ViewHolder {

		final TextView authorView;
		final TextView contentView;
		final TextView urlView;

		ViewHolder(View view) {
			authorView = (TextView) view.findViewById(R.id.author);
			contentView = (TextView) view.findViewById(R.id.content);
			urlView = (TextView) view.findViewById(R.id.url);
		}

	}

	private static final int REVIEWS_LOADER = 0;
	final String TAG = this.getClass().getSimpleName();
	private Uri mUri;
	private LinearLayout reviewsLayout;
	private TextView titleView;

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
		View view = inflater.inflate(R.layout.fragment_section_layout, container, false);
		reviewsLayout = (LinearLayout) view.findViewById(R.id.sectionLayout);
		titleView = (TextView) view.findViewById(R.id.subtitleView);
		titleView.setText(R.string.reviews);
		return view;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		reviewsLayout.removeAllViews();
		if (data == null && !data.moveToFirst()) {
			return;
		}
		Log.d(TAG, String.format("review count: %s for movie_id: %s", data.getCount(), mUri.getLastPathSegment()));
		if (data.getCount() < 1) {
			titleView.setVisibility(View.GONE);
			return;
		}
		while (data.moveToNext()) {
			View reviewDataView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_review, reviewsLayout, false);
			ViewHolder viewHolder = new ViewHolder(reviewDataView);
			reviewDataView.setTag(viewHolder);
			final String content = data.getString(data.getColumnIndex(ReviewsEntry.COLUMN_CONTENT));
			viewHolder.contentView.setText(content.length() <= 100 ? content : String.format("%s...", content.substring(0, 100)));
			viewHolder.authorView.setText(data.getString(data.getColumnIndex(ReviewsEntry.COLUMN_AUTHOR)));
			final String urlString = data.getString(data.getColumnIndex(ReviewsEntry.COLUMN_URL));
			viewHolder.urlView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
					startActivity(browserIntent);
				}
			});
			reviewsLayout.addView(reviewDataView);

		}
		data.close();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

}
