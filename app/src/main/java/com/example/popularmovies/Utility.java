package com.example.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesContract.FavoritesEntry;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;

/**
 * Utility methods
 * <p/>
 * Created by Asim Qureshi.
 */
public class Utility {

	private static final String TAG = Utility.class.getSimpleName();

	@NonNull
	public static ContentValues createFavoritesValues(@NonNull String movieId) {
		ContentValues favoriteValues = new ContentValues();
		favoriteValues.put(FavoritesEntry._ID, movieId);
		return favoriteValues;
	}

	@NonNull
	public static Uri getMovieInfoUri(@NonNull String criteria) {
		if (criteria.equals(MoviesContract.TOP_RATED)) {
			return MoviesEntry.buildHighestRatedMovies();
		} else if (criteria.equals(MoviesContract.POPULAR)) {
			return MoviesEntry.buildMostPopularMovies();
		}
		return MoviesEntry.buildFavoriteMovies();
	}

	@NonNull
	public static Uri getMovieInfoUri(@NonNull Context context) {
		if (!isNetworkAvailable(context)) {
			return MoviesEntry.buildFavoriteMovies();
		}
		return getMovieInfoUri(getPreferredCriteria(context));
	}

	@NonNull
	public static String getPreferredCriteria(@NonNull Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(context.getString(R.string.pref_search_criteria_key), context.getString(R.string.pref_search_criteria_default));
	}

	/**
	 * Helper method to convert the database representation of the date into something to display
	 * to users.  As classy and polished a user experience as "20140102" is, we can do better.
	 *
	 * @return a user-friendly representation of the date.
	 */
	@NonNull
	public static String getYear(@NonNull String dateString) {
		String year = "";
		try {
			year = dateString.split("-")[0];
		} catch (Exception e) {
			Log.e(TAG, "caught an exception: ", e);
		}
		return year;
	}

	public static boolean isNetworkAvailable(@NonNull Context context) {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
