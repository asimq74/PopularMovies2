package com.example.popularmovies;

import android.content.Intent;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

/**
 * A fragment representing a list of Items.
 * <p />
 */
public class MovieTrailersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public class ViewHolder extends RecyclerView.ViewHolder {

		public final TextView trailerNameView;

		public ViewHolder(View view) {
			super(view);
			trailerNameView = (TextView) view.findViewById(R.id.trailerName);
		}

	}
	private static final int VIDEOS_LOADER = 0;
	final String TAG = this.getClass().getSimpleName();
	private Uri mUri;
	private TextView titleView;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		getLoaderManager().initLoader(VIDEOS_LOADER, null, this);
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
		final Uri buildVideosByIdUri = VideosEntry.buildVideosById(movieId);
		return new CursorLoader(getActivity(),
				buildVideosByIdUri,
				null,
				null,
				null,
				null);
	}

	private LinearLayout videosLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_section_layout, container, false);
		videosLayout = (LinearLayout) view.findViewById(R.id.sectionLayout);
		titleView = (TextView) view.findViewById(R.id.subtitleView);
		titleView.setText(R.string.trailers);
		return view;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		TextView trailerNameView;
		videosLayout.removeAllViews();
		if (data == null && !data.moveToFirst()) {
			return;
		}
		Log.d(TAG, String.format("trailers count: %s for movie_id: %s", data.getCount(), mUri.getLastPathSegment()));
		if (data.getCount() < 1) {
			titleView.setVisibility(View.GONE);
			return;
		}
		data.moveToFirst();
		while (!data.isAfterLast()) {
			View trailerDataView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_trailer, videosLayout, false);
			trailerNameView = (TextView) trailerDataView.findViewById(R.id.trailerName);
			final String name = data.getString(data.getColumnIndex(VideosEntry.COLUMN_NAME));
			trailerNameView.setText(name);
			videosLayout.addView(trailerDataView);
			data.moveToNext();
		}
		data.close();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

}
