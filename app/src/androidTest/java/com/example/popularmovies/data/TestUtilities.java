package com.example.popularmovies.data;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.popularmovies.data.MoviesContract.MoviesEntry;

/**
 * Created by U1C306 on 11/7/2016.
 */

public class TestUtilities extends AndroidTestCase {

	public static final int TEST_ID = 284052;
	final static String LOG_TAG = TestUtilities.class.getSimpleName();


	static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
		Set<Entry<String, Object>> valueSet = expectedValues.valueSet();
		for (Map.Entry<String, Object> entry : valueSet) {
			String columnName = entry.getKey();
			int idx = valueCursor.getColumnIndex(columnName);
			assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
			Log.i(LOG_TAG, String.format("Column: %s idx: %s", columnName, idx));
			String expectedValue = entry.getValue().toString();
			assertEquals("Value '" + entry.getValue().toString() +
					"' did not match the expected value '" +
					expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
			Log.i(LOG_TAG, String.format("expectedValue: %s actualValue: %s", entry.getValue().toString(), valueCursor.getString(idx)));
		}
	}

	public static ContentValues createMoviesTestValues() {
		ContentValues movieValues = new ContentValues();
		movieValues.put(MoviesEntry.COLUMN_ID, TEST_ID);
		movieValues.put(MoviesEntry.COLUMN_ADULT, false);
		movieValues.put(MoviesEntry.COLUMN_BACKDROP_PATH, "/hETu6AxKsWAS42tw8eXgLUgn4Lo.jpg");
		movieValues.put(MoviesEntry.COLUMN_ORIGINAL_LANGUAGE, "en");
		movieValues.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, "Doctor Strange");
		movieValues.put(MoviesEntry.COLUMN_OVERVIEW, "After his career is destroyed, a brilliant but arrogant surgeon gets a new lease on life when a sorcerer takes him under his wing and trains him to defend the world against evil.");
		movieValues.put(MoviesEntry.COLUMN_POPULARITY, 72.035408);
		movieValues.put(MoviesEntry.COLUMN_POSTER_PATH, "/xfWac8MTYDxujaxgPVcRD9yZaul.jpg");
		movieValues.put(MoviesEntry.COLUMN_RELEASE_DATE, "2016-10-25");
		movieValues.put(MoviesEntry.COLUMN_TITLE, "Doctor Strange");
		movieValues.put(MoviesEntry.COLUMN_VIDEO, false);
		movieValues.put(MoviesEntry.COLUMN_VOTE_AVERAGE, 7);
		movieValues.put(MoviesEntry.COLUMN_VOTE_COUNT, 536);
		return movieValues;
	}

}
