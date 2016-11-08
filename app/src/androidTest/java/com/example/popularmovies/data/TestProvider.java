package com.example.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

/**
 * Created by U1C306 on 11/8/2016.
 */

public class TestProvider extends AndroidTestCase {

	// Since we want each test to start with a clean slate, run deleteAllRecords
	// in setUp (called by the test runner before each test).
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		deleteAllRecords();
	}

	/*
		Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
		you have implemented delete functionality there.
 */
	public void deleteAllRecords() {
		deleteAllRecordsFromProvider();
	}

	/*
		 This helper function deletes all records from both database tables using the ContentProvider.
		 It also queries the ContentProvider to make sure that the database has been successfully
		 deleted, so it cannot be used until the Query and Delete functions have been written
		 in the ContentProvider.

		 Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
		 the delete functionality in the ContentProvider.
	 */
	public void deleteAllRecordsFromProvider() {
		mContext.getContentResolver().delete(
				MoviesEntry.CONTENT_URI,
				null,
				null
		);
		mContext.getContentResolver().delete(
				VideosEntry.CONTENT_URI,
				null,
				null
		);
		mContext.getContentResolver().delete(
				ReviewsEntry.CONTENT_URI,
				null,
				null
		);

		Cursor cursor = mContext.getContentResolver().query(
				MoviesEntry.CONTENT_URI,
				null,
				null,
				null,
				null
		);
		assertEquals("Error: Records not deleted from Movies table during delete", 0, cursor.getCount());
		cursor.close();

		cursor = mContext.getContentResolver().query(
				VideosEntry.CONTENT_URI,
				null,
				null,
				null,
				null
		);
		assertEquals("Error: Records not deleted from Videos table during delete", 0, cursor.getCount());
		cursor.close();

		cursor = mContext.getContentResolver().query(
				ReviewsEntry.CONTENT_URI,
				null,
				null,
				null,
				null
		);
		assertEquals("Error: Records not deleted from Reviews table during delete", 0, cursor.getCount());
		cursor.close();
	}

	/*
		This test uses the database directly to insert and then uses the ContentProvider to
		read out the data.  Uncomment this test to see if the basic weather query functionality
		given in the ContentProvider is working correctly.
 */
	public void testBasicMovieQuery() {
		// insert our test records into the database
		MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues testValues = TestUtilities.createMoviesTestValues();
//		long locationRowId = TestUtilities.insertNorthPoleLocationValues(mContext);
//
//		// Fantastic.  Now that we have a location, add some weather!
//		ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);
//
//		long weatherRowId = db.insert(WeatherEntry.TABLE_NAME, null, weatherValues);
//		assertTrue("Unable to Insert WeatherEntry into the Database", weatherRowId != -1);
//
//		db.close();
//
//		// Test the basic content provider query
//		Cursor weatherCursor = mContext.getContentResolver().query(
//				WeatherEntry.CONTENT_URI,
//				null,
//				null,
//				null,
//				null
//		);
//
//		// Make sure we get the correct cursor out of the database
//		TestUtilities.validateCursor("testBasicWeatherQuery", weatherCursor, weatherValues);
	}
}
