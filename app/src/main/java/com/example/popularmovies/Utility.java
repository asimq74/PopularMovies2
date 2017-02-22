package com.example.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by U1C306 on 2/22/2017.
 */

public class Utility {
	public static String getPreferredCriteria(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(context.getString(R.string.pref_search_criteria_key), context.getString(R.string.pref_search_criteria_default));
	}
}
