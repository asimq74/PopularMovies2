package com.example.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main Activity displaying Popular Movies Detail
 * <p/>
 * Created by Asim Qureshi.
 */
public class MovieDetailActivity extends ActionBarActivity {

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
			if (reviewsFragment == null || !(reviewsFragment instanceof MoviesGridFragment)) {
				getSupportFragmentManager().beginTransaction().add(R.id.movie_reviews_fragment,
						new MoviesGridFragment()).commit();
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
