package com.example.popularmovies.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

/**
 * Test Content Provider for movies
 * <p>
 * Created by Asim Qureshi
 */

public class TestProvider extends AndroidTestCase {

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

	// Since we want each test to start with a clean slate, run deleteAllRecords
	// in setUp (called by the test runner before each test).
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		deleteAllRecords();
	}

	/*
		This test uses the database directly to insert and then uses the ContentProvider to
		read out the data.  Uncomment this test to see if the basic weather query functionality
		given in the ContentProvider is working correctly.
 */
	public void testBasicMovieQuery() {
		testBasicQuery(MoviesEntry.TABLE_NAME, MoviesEntry.CONTENT_URI, TestUtilities.createMoviesTestValues());
	}

	public void testBasicQuery(String tableName, Uri contentUri, ContentValues testValues) {
		// insert our test records into the database
		MoviesDbHelper dbHelper = new MoviesDbHelper(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		long rowId = db.insert(tableName, null, testValues);
		assertTrue(String.format("Unable to Insert %s into the Database", tableName), rowId != -1);

		db.close();

		// Test the basic content provider query
		Cursor cursor = mContext.getContentResolver().query(
				contentUri,
				null,
				null,
				null,
				null
		);

		// Make sure we get the correct cursor out of the database
		TestUtilities.validateCursor(String.format("testBasicQuery for %s", tableName), cursor, testValues);
	}

	public void testBasicReviewsQuery() {
		testBasicQuery(ReviewsEntry.TABLE_NAME, ReviewsEntry.CONTENT_URI, TestUtilities.createReviewTestValues());
	}

	public void testBasicTrailersQuery() {
		testBasicQuery(VideosEntry.TABLE_NAME, VideosEntry.CONTENT_URI, TestUtilities.createVideoTestValues());
	}


	public static final String LOG_TAG = TestProvider.class.getSimpleName();

	/*
			 This test uses the provider to insert and then update the data. Uncomment this test to
			 see if your update location is functioning correctly.
		*/
	public void testUpdateMovie() {
		// Create a new map of values, where column names are the keys
		ContentValues values = TestUtilities.createMoviesTestValues();

		Uri movieUri = mContext.getContentResolver().insert(MoviesEntry.CONTENT_URI, values);
		long movieRowId = ContentUris.parseId(movieUri);

		// Verify we got a row back.
		assertTrue(movieRowId != -1);
		Log.d(LOG_TAG, "New row id: " + movieRowId);

		ContentValues updatedValues = new ContentValues(values);
		updatedValues.put(MoviesEntry._ID, movieRowId);
		updatedValues.put(MoviesEntry.COLUMN_TITLE, "Birth of a Nation");

		// Create a cursor with observer to make sure that the content provider is notifying
		// the observers as expected
		Cursor movieCursor = mContext.getContentResolver().query(MoviesEntry.CONTENT_URI, null, null, null, null);

		TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
		movieCursor.registerContentObserver(tco);

		int count = mContext.getContentResolver().update(
				MoviesEntry.CONTENT_URI, updatedValues, MoviesEntry._ID + "= ?",
				new String[] { Long.toString(movieRowId)});
		assertEquals(count, 1);

		movieCursor.unregisterContentObserver(tco);
		movieCursor.close();

		// A cursor is your primary interface to the query results.
		Cursor cursor = mContext.getContentResolver().query(
				MoviesEntry.CONTENT_URI,
				null,   // projection
				MoviesEntry._ID + " = " + movieRowId,
				null,   // Values for the "where" clause
				null    // sort order
		);

		TestUtilities.validateCursor("testUpdateMovie.  Error validating movies entry update.",
				cursor, updatedValues);

		cursor.close();
	}

}