package com.example.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main Activity for Popular Movies
 * <p/>
 * Created by Asim Qureshi.
 */
public class MainActivity extends ActionBarActivity {

    private String currentMovieSearchCriteria = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentMovieSearchCriteria = Utility.getPreferredCriteria(this);
        setContentView(R.layout.activity_main);
        MoviesGridFragment moviesGridFragment = (MoviesGridFragment) getSupportFragmentManager().findFragmentById(R.id.movies_grid_fragment);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String newMovieSearchCriteria = Utility.getPreferredCriteria(this);
        if (newMovieSearchCriteria != null && !newMovieSearchCriteria.equals(currentMovieSearchCriteria)) {
            MoviesGridFragment moviesGridFragment = (MoviesGridFragment) getSupportFragmentManager().findFragmentById(R.id.movies_grid_fragment);
            if (null != moviesGridFragment) {
                moviesGridFragment.onMoviesSortCriteriaChanged();
            }
        }
			currentMovieSearchCriteria = newMovieSearchCriteria;
    }
}
