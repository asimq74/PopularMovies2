package com.example.popularmovies;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesContract.HighestRatedEntry;
import com.example.popularmovies.data.MoviesContract.MostPopularEntry;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;

/**
 * Created by U1C306 on 2/22/2017.
 */

public class Utility {

	public final static String POPULAR = "popular";
	public final static String TOP_RATED = "top_rated";

	//create movie id selection
	public static String createMovieIdSelection(List<String> ids) {
		return MoviesContract.MoviesEntry.TABLE_NAME +
				"." + MoviesEntry._ID + " IN (" + Utility.makePlaceholders(ids.size()) + ")";
	}

	public static Uri determineDeleteUriBasedOnCriteria(String criteria) {
		return criteria.equals(POPULAR) ? MostPopularEntry.removeAllMostPopular() : HighestRatedEntry.removeAllHighestRated();
	}

	public static Uri determineContentUriBasedOnCriteria(String criteria) {
		return criteria.equals(POPULAR) ? MostPopularEntry.CONTENT_URI : HighestRatedEntry.CONTENT_URI;
	}

	public static String determineTableBasedOnCriteria(String criteria) {
		return criteria.equals(POPULAR) ? MostPopularEntry.TABLE_NAME : HighestRatedEntry.TABLE_NAME;
	}

	public static String getPreferredCriteria(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(context.getString(R.string.pref_search_criteria_key), context.getString(R.string.pref_search_criteria_default));
	}

	private static String makePlaceholders(int length) {
		if (length < 1) {
			// It will lead to an invalid query anyway ..
			throw new RuntimeException("No placeholders");
		} else {
			StringBuilder sb = new StringBuilder(length * 2 - 1);
			sb.append("?");
			for (int i = 1; i < length; i++) {
				sb.append(",?");
			}
			return sb.toString();
		}
	}
}
