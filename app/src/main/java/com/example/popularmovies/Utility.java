package com.example.popularmovies;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;

/**
 * Created by U1C306 on 2/22/2017.
 */

public class Utility {
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

	//create movie id selection
	public static String createMovieIdSelection(List<String> ids) {
		return MoviesContract.MoviesEntry.TABLE_NAME +
				"." + MoviesEntry._ID + "IN (" + Utility.makePlaceholders(ids.size()) +")";
	}
}
