package com.example.popularmovies;

import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.popularmovies.data.MoviesContract.FavoritesEntry;

/**
 * Created by U1C306 on 2/22/2017.
 */

public class Utility {

	private static final String TAG = Utility.class.getSimpleName();

	public static ContentValues createFavoritesValues(String movieId, boolean favorite) {
		ContentValues favoriteValues = new ContentValues();
		favoriteValues.put(FavoritesEntry._ID, UUID.randomUUID().toString());
		favoriteValues.put(FavoritesEntry.COLUMN_MOVIE_ID, movieId);
		favoriteValues.put(FavoritesEntry.COLUMN_FAVORITE, favorite ? 1 : 0);
		return favoriteValues;
	}

	static String getPreferredCriteria(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(context.getString(R.string.pref_search_criteria_key), context.getString(R.string.pref_search_criteria_default));
	}

	/**
	 * Helper method to convert the database representation of the date into something to display
	 * to users.  As classy and polished a user experience as "20140102" is, we can do better.
	 *
	 * @return a user-friendly representation of the date.
	 */
	static String getYear(String dateString) {
		String year = "";
		try {
			year = dateString.split("-")[0];
		} catch (Exception e) {
			Log.e(TAG, "caught an exception: ", e);
		}
		return year;
	}

}
