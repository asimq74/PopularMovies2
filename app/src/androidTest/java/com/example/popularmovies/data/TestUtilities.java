package com.example.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.popularmovies.data.MoviesContract.FavoritesEntry;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by U1C306 on 11/7/2016.
 */

public class TestUtilities extends AndroidTestCase {

	public static final int TEST_ID = 284052;
	final static String LOG_TAG = TestUtilities.class.getSimpleName();


	/*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the WeatherContract as well as the WeatherDbHelper.
     */
	static long insertReviewValues(Context context) {
		// insert our test records into the database
		MoviesDbHelper dbHelper = new MoviesDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues testValues = TestUtilities.createReviewTestValues();

		long reviewsRowId;
		reviewsRowId = db.insert(MoviesContract.ReviewsEntry.TABLE_NAME, null, testValues);

		// Verify we got a row back.
		assertTrue("Error: Failure to insert Reviews Values", reviewsRowId != -1);

		return reviewsRowId;
	}

	static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
		assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
		validateCurrentRecord(error, valueCursor, expectedValues);
		valueCursor.close();
	}


	/*
			 Students: The functions we provide inside of TestProvider use this utility class to test
			 the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
			 CTS tests.

			 Note that this only tests that the onChange function is called; it does not test that the
			 correct Uri is returned.
		*/
	static class TestContentObserver extends ContentObserver {
		final HandlerThread mHT;
		boolean mContentChanged;

		static TestContentObserver getTestContentObserver() {
			HandlerThread ht = new HandlerThread("ContentObserverThread");
			ht.start();
			return new TestContentObserver(ht);
		}

		private TestContentObserver(HandlerThread ht) {
			super(new Handler(ht.getLooper()));
			mHT = ht;
		}

		// On earlier versions of Android, this onChange method is called
		@Override
		public void onChange(boolean selfChange) {
			onChange(selfChange, null);
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			mContentChanged = true;
		}

	}

	static TestContentObserver getTestContentObserver() {
		return TestContentObserver.getTestContentObserver();
	}


	static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
		Set<Entry<String, Object>> valueSet = expectedValues.valueSet();
		for (Map.Entry<String, Object> entry : valueSet) {
			String columnName = entry.getKey();
			int idx = valueCursor.getColumnIndex(columnName);
			assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
			Log.i(LOG_TAG, String.format("Column: %s idx: %s", columnName, idx));
			String expectedValue = entry.getValue().toString();
			assertEquals(entry.getKey() + " Value '" + entry.getValue().toString() +
					"' did not match the expected value '" +
					expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
			Log.i(LOG_TAG, String.format("expectedValue: %s actualValue: %s", entry.getValue().toString(), valueCursor.getString(idx)));
		}
	}

	public static ContentValues createMoviesTestValues() {
		ContentValues movieValues = new ContentValues();
		movieValues.put(MoviesEntry._ID, TEST_ID);
		movieValues.put(MoviesEntry.COLUMN_ADULT, 0);
		movieValues.put(MoviesEntry.COLUMN_BACKDROP_PATH, "/hETu6AxKsWAS42tw8eXgLUgn4Lo.jpg");
		movieValues.put(MoviesEntry.COLUMN_ORIGINAL_LANGUAGE, "en");
		movieValues.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, "Doctor Strange");
		movieValues.put(MoviesEntry.COLUMN_OVERVIEW, "After his career is destroyed, a brilliant but arrogant surgeon gets a new lease on life when a sorcerer takes him under his wing and trains him to defend the world against evil.");
		movieValues.put(MoviesEntry.COLUMN_POPULARITY, Math.round(72.035408));
		movieValues.put(MoviesEntry.COLUMN_POSTER_PATH, "/xfWac8MTYDxujaxgPVcRD9yZaul.jpg");
		movieValues.put(MoviesEntry.COLUMN_RELEASE_DATE, "2016-10-25");
		movieValues.put(MoviesEntry.COLUMN_TITLE, "Doctor Strange");
		movieValues.put(MoviesEntry.COLUMN_VIDEO, 0);
		movieValues.put(MoviesEntry.COLUMN_VOTE_AVERAGE, 7);
		movieValues.put(MoviesEntry.COLUMN_VOTE_COUNT, 536);
		movieValues.put(MoviesEntry.COLUMN_SEARCH_CRITERIA, "popular");
		return movieValues;
	}

	static public final int BULK_INSERT_RECORDS_TO_INSERT = 10;

	public static ContentValues[] createBulkInsertMoviesTestValues() {
		ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];
		for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
			ContentValues movieValues = new ContentValues();
			movieValues.put(MoviesEntry._ID, i+1);
			movieValues.put(MoviesEntry.COLUMN_ADULT, 0);
			movieValues.put(MoviesEntry.COLUMN_BACKDROP_PATH, "/hETu6AxKsWAS42tw8eXgLUgn4Lo.jpg");
			movieValues.put(MoviesEntry.COLUMN_ORIGINAL_LANGUAGE, "en");
			movieValues.put(MoviesEntry.COLUMN_ORIGINAL_TITLE, "Doctor Strange");
			movieValues.put(MoviesEntry.COLUMN_OVERVIEW, "After his career is destroyed, a brilliant but arrogant surgeon gets a new lease on life when a sorcerer takes him under his wing and trains him to defend the world against evil.");
			movieValues.put(MoviesEntry.COLUMN_POPULARITY, Math.round(72.035408));
			movieValues.put(MoviesEntry.COLUMN_POSTER_PATH, "/xfWac8MTYDxujaxgPVcRD9yZaul.jpg");
			movieValues.put(MoviesEntry.COLUMN_RELEASE_DATE, "2016-10-25");
			movieValues.put(MoviesEntry.COLUMN_TITLE, "Doctor Strange");
			movieValues.put(MoviesEntry.COLUMN_VIDEO, 0);
			movieValues.put(MoviesEntry.COLUMN_VOTE_AVERAGE, 7);
			movieValues.put(MoviesEntry.COLUMN_VOTE_COUNT, 536);
			returnContentValues[i] = movieValues;
		}
		return returnContentValues;
	}

	public static ContentValues createReviewTestValues() {
		ContentValues reviewValues = new ContentValues();
		reviewValues.put(ReviewsEntry._ID, "581bbdbbc3a36805c60001f1");
		reviewValues.put(ReviewsEntry.COLUMN_MOVIE_ID, TEST_ID);
		reviewValues.put(ReviewsEntry.COLUMN_AUTHOR, "iheardthatmoviewas");
		reviewValues.put(ReviewsEntry.COLUMN_CONTENT, "With each new Marvel film one could expect the studio to push the limits of what a superhero film should consist of. Films such as Captain America: Winter Solider, Guardians of the Galaxy and Ant-Man all consisted of elements that we have never seen within a superhero film before. Now, Marvel Studios attempts to push the envelop once more by leaving the world of high tech armors and super soldiers and entering the mystical world of magic. Leading this charge is Benedict Cumberbatch's Doctor Stephen Strange and hopefully he could answer why there is this strange mystical feeling that we have seen this all before.\r\n\r\n\r\n\r\n> From Marvel comes “Doctor Strange,” the story of world-famous neurosurgeon Dr. Stephen Strange whose life changes forever after a horrific car accident robs him of the use of his hands. When traditional medicine fails him, he is forced to look for healing, and hope, in an unlikely place—a mysterious enclave known as Kamar-Taj. He quickly learns that this is not just a center for healing but also the front line of a battle against unseen dark forces bent on destroying our reality. Before long Strange—armed with newly acquired magical powers—is forced to choose whether to return to his life of fortune and status or leave it all behind to defend the world as the most powerful sorcerer in existence.\r\n\r\nI am going to start off the review by discussing the only strong positive thing that came out of Doctor Strange and its by far the visuals. Beautiful vibrant colors pop at you throughout the film, the costume design is beyond extravagant and the framing is spot on. Doctor Strange and his fellow Sorcerers Supremes powers aren't fully on display until they enter the Mirror Dimension and my god, Tony Stark and Bruce Banner would hate this place. As they are men of science, they would hate that all rules of physics are thrown out the window as our Sorcerers Supremes are capable of breaking buildings apart and reforming to however they please. It is hard to not reference Inception but no one here is dreaming, these visuals are being created by the hand of mystical men and they use it to their power.\r\n\r\nIn all honestly, pushing the envelope when it comes to visuals was not a difficult task at all. Heck, if they didn't I would have been extremely disappointed as it was an obvious opportunity to take advantage of since we are dealing with magic. I am still extremely disappointed regardless as director Scott Derrickson completely missed another obvious opportunity to push the envelope this time in regards of character development.\r\n\r\nDoctor Stephen Strange is very much like a Tony Stark and other superheroes we have seen in films before as he is arrogant, egoistical and full of it. After Strange's accident in which he suffered extreme nerve damage to both hands, he dishes out every penny to his name to repair his hands in hopes he could continue his career as neurosurgeon. Every attempt fails and Strange becomes desperate enough to head to Nepal and finds himself under the guidance of Tilda Swinton's The Ancient One. Learning a whole new concept in the mystic arts forces Strange to reshape his way of thinking but Scott Derrickson does not let this have Strange reshape who he is as a person as well.\r\n\r\nDoctor Strange should have found himself being more like a Steve Rogers towards the end of the film as learning the mystic arts and his accident alone should have broken his ego. But instead everything still comes easy to him as he masters magic with ease with very little tension. He has a couple hiccups in the beginning but once The Ancient One puts him under her wing it is smooth sailing and Strange is stealing texts, wielding powerful weapons and defeating Supremes with decades of training with ease.\r\n\r\nYou cannot say that Strange was destined for the mystic arts and that is why everything came so easy for him. Strange is only learning the mystic arts due to circumstance. If he doesn't get into an accident, which happened to him due to being careless, he doesn't find himself in Nepal. Harry Potter was destined for magic, not Strange. Strange fails to grow as a character because he continues to be proven right and no consequences seem to happen due to his actions.\r\n\r\nIt is hard to get into the character development of our supporting characters Benedict Wong's Wong and Chiwetel Ejiofor's Mordo as I would utterly spoil the film for you. All I would say though is that they were both mishandled and deserved better developments. It is a shame too because Wong and Ejiofor are superb actors but wasting talent is something Marvel Studios is used to.\r\n\r\nWhen will Marvel Studios get a villain right?  With Mads Mikkelsen playing our lead villain, Kaecilius, I thought we might finally get a cinematic Marvel villain that is on par to Killgrave and Kingpin over at Marvel's television department. Ultimately, Kaecilius and Doctor Strange end up fighting due to the most silliest cliche: being at the wrong place at the wrong time. The two individuals meet at the New York Sanctum Sanctorum in which Strange accidentally gets blown in to. Kaecilius is there to kill the protector and Sanctum Sanctorum itself and has no knowledge of who Strange is. In fact, he honestly believes Strange's name is in fact \"Mister Doctor.\" Still learning his craft, Strange is capable of putting up a better fight than the protector who should be a matter. I mean, he is a protector after all. There are no stakes for Strange, strange.\r\n\r\nVisually appealing could only get you so far as Doctor Strange fails to focus on the development of their characters that they made so appealing to the eye. Doctor Strange will become a huge asset to the Marvel Cinematic Universe and lets hope we see the rest of Strange's development when he casts his next spell. There is so much untapped potential in Strange's character, we just haven't seen the best of him yet.");
		reviewValues.put(ReviewsEntry.COLUMN_URL, "https://www.themoviedb.org/review/581bbdbbc3a36805c60001f1");
		return reviewValues;
	}

	public static ContentValues createReviewTestValues2() {
		ContentValues reviewValues = new ContentValues();
		reviewValues.put(ReviewsEntry._ID, "345345345a36805c60001f1");
		reviewValues.put(ReviewsEntry.COLUMN_MOVIE_ID, TEST_ID);
		reviewValues.put(ReviewsEntry.COLUMN_AUTHOR, "dumbrewviewer");
		reviewValues.put(ReviewsEntry.COLUMN_CONTENT, "I hated this movie because I am dumb.");
		reviewValues.put(ReviewsEntry.COLUMN_URL, "https://www.themoviedb.org/review/581bbdbbc3a36805c60001f1");
		return reviewValues;
	}

	public static ContentValues createReviewTestValues3() {
		ContentValues reviewValues = new ContentValues();
		reviewValues.put(ReviewsEntry._ID, "5765756756a36805c60001f1");
		reviewValues.put(ReviewsEntry.COLUMN_MOVIE_ID, TEST_ID);
		reviewValues.put(ReviewsEntry.COLUMN_AUTHOR, "smartreviewer");
		reviewValues.put(ReviewsEntry.COLUMN_CONTENT, "I loved this movie because I am smart.");
		reviewValues.put(ReviewsEntry.COLUMN_URL, "https://www.themoviedb.org/review/581bbdbbc3a36805c60001f1");
		return reviewValues;
	}

	public static List<ContentValues> createMultipleReviewTestValues() {
		List<ContentValues> contentValuesList = new ArrayList<>();
		contentValuesList.add(createReviewTestValues());
		contentValuesList.add(createReviewTestValues2());
		contentValuesList.add(createReviewTestValues3());
		return contentValuesList;
	}

	public static ContentValues createVideoTestValues() {
		ContentValues videoValues = new ContentValues();
		videoValues.put(VideosEntry._ID, "57992d84c3a3687e5c003a3b");
		videoValues.put(VideosEntry.COLUMN_MOVIE_ID, TEST_ID);
		videoValues.put(VideosEntry.COLUMN_NAME, "Doctor Strange Official Trailer 2");
		videoValues.put(VideosEntry.COLUMN_KEY, "HSzx-zryEgM");
		videoValues.put(VideosEntry.COLUMN_SITE, "YouTube");
		videoValues.put(VideosEntry.COLUMN_SIZE, 1080);
		videoValues.put(VideosEntry.COLUMN_TYPE, "Trailer");
		return videoValues;
	}

	public static ContentValues createFavoritesTestValues() {
		ContentValues favoriteValues = new ContentValues();
		favoriteValues.put(FavoritesEntry._ID, "57992d84c3a3687e5c003a3b");
		return favoriteValues;
	}

	public static ContentValues createFavoriteMoviesTestValues() {
		ContentValues favoriteMoviesTestValues = new ContentValues();
		favoriteMoviesTestValues.putAll(createMoviesTestValues());
		favoriteMoviesTestValues.putAll(createFavoritesTestValues());
		return favoriteMoviesTestValues;
	}

}
