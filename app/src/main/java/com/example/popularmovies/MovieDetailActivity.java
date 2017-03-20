package com.example.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmovies.MovieTrailersFragment.MovieTrailerCallback;

/**
 * Main Activity displaying Popular Movies Detail
 * <p/>
 * Created by Asim Qureshi.
 */
public class MovieDetailActivity extends ActionBarActivity implements MovieTrailerCallback {

	private ShareActionProvider mShareActionProvider;

	public Intent createShareFirstTrailerIntent() {
		Intent intent = new Intent();
		final MovieDetailActivityFragment detailActivityFragment = (MovieDetailActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
		if (null != detailActivityFragment) {
			intent = detailActivityFragment.createShareFirstTrailerIntent();
		}
		return intent;
	}

	@Override
	public String getFirstTrailerUrl() {
		String firstTrailerUrl = "";
		final MovieTrailersFragment trailersFragment = (MovieTrailersFragment) getSupportFragmentManager().findFragmentById(R.id.movie_trailers_fragment);
		if (null != trailersFragment) {
			firstTrailerUrl = trailersFragment.getFirstTrailerUrl();
		}
		return firstTrailerUrl;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_detail);
		if (savedInstanceState == null) {
			final Fragment mainFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
			if (mainFragment == null || !(mainFragment instanceof MovieDetailActivityFragment)) {
				getSupportFragmentManager().beginTransaction().add(R.id.fragment,
						new MovieDetailActivityFragment()).commit();
			}
			final Fragment reviewsFragment = getSupportFragmentManager().findFragmentById(R.id.movie_reviews_fragment);
			if (reviewsFragment == null || !(reviewsFragment instanceof MovieReviewsFragment)) {
				getSupportFragmentManager().beginTransaction().add(R.id.movie_reviews_fragment,
						new MovieReviewsFragment()).commit();
			}
			final Fragment trailersFragment = getSupportFragmentManager().findFragmentById(R.id.movie_trailers_fragment);
			if (trailersFragment == null || !(trailersFragment instanceof MovieTrailersFragment)) {
				getSupportFragmentManager().beginTransaction().add(R.id.movie_trailers_fragment,
						new MovieTrailersFragment()).commit();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_movie_detail, menu);

		// Retrieve the share menu item
		MenuItem menuItem = menu.findItem(R.id.action_share);

		// Get the provider and hold onto it to set/change the share intent.
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

		// If onLoadFinished happens before this, we can go ahead and set the share intent now.
		if (getFirstTrailerUrl() != null) {
			mShareActionProvider.setShareIntent(createShareFirstTrailerIntent());
		}
		return true;
	}
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//
//		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}
//
//		if (id == R.id.action_share) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
