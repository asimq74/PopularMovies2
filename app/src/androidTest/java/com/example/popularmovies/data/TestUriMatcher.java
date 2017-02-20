package com.example.popularmovies.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.popularmovies.data.MoviesContract.FavoritesEntry;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

/**
 * Created by U1C306 on 11/4/2016.
 */

public class TestUriMatcher extends AndroidTestCase {

	private static final Uri TEST_MOVIES_DIR = MoviesEntry.CONTENT_URI;
	private static final int TEST_MOVIE_ID = 40101;
	private static final Uri TEST_MOVIE_BY_ID = MoviesEntry.buildMovieById(TEST_MOVIE_ID);
	private static final Uri TEST_MOVIE_REVIEWS = ReviewsEntry.buildReviewsById(TEST_MOVIE_ID);
	private static final Uri TEST_MOVIE_TRAILERS = VideosEntry.buildVideosById(TEST_MOVIE_ID);
	private static final Uri TEST_FAVORITE_BY_ID = FavoritesEntry.buildFavoritesById(TEST_MOVIE_ID);
	private static final Uri TEST_REMOVE_FAVORITE_BY_ID = FavoritesEntry.removeFavoriteById(TEST_MOVIE_ID);

	/*
			Students: This function tests that your UriMatcher returns the correct integer value
			for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
			ready to test your UriMatcher.
	 */
	public void testUriMatcher() {
		UriMatcher testMatcher = MoviesProvider.buildUriMatcher();
		Log.d(getClass().getSimpleName(), String.format("TEST_MOVIE_TRAILERS: %s MoviesProvider.MOVIE_TRAILERS: %s", TEST_MOVIE_TRAILERS, MoviesProvider.MOVIE_TRAILERS));
		assertEquals("Error: The LOCATION URI was matched incorrectly.",
				testMatcher.match(TEST_MOVIE_TRAILERS), MoviesProvider.MOVIE_TRAILERS);
		Log.d(getClass().getSimpleName(), String.format("TEST_MOVIE_REVIEWS: %s MoviesProvider.MOVIE_REVIEWS: %s", TEST_MOVIE_REVIEWS, MoviesProvider.MOVIE_REVIEWS));
		assertEquals("Error: The LOCATION URI was matched incorrectly.",
				testMatcher.match(TEST_MOVIE_REVIEWS), MoviesProvider.MOVIE_REVIEWS);
		Log.d(getClass().getSimpleName(), String.format("TEST_MOVIE_BY_ID: %s MoviesProvider.MOVIE_BY_ID: %s", TEST_MOVIE_BY_ID, MoviesProvider.MOVIE_BY_ID));
		assertEquals("Error: The LOCATION URI was matched incorrectly.",
				testMatcher.match(TEST_MOVIE_BY_ID), MoviesProvider.MOVIE_BY_ID);
		Log.d(getClass().getSimpleName(), String.format("TEST_MOVIES_DIR: %s MoviesProvider.MOVIES: %s", TEST_MOVIES_DIR, MoviesProvider.MOVIES));
		assertEquals("Error: The LOCATION URI was matched incorrectly.",
				testMatcher.match(TEST_MOVIES_DIR), MoviesProvider.MOVIES);
		Log.d(getClass().getSimpleName(), String.format("TEST_FAVORITE_BY_ID: %s MoviesProvider.FAVORITE_BY_ID: %s", TEST_FAVORITE_BY_ID, MoviesProvider.FAVORITE_BY_ID));
		assertEquals("Error: The LOCATION URI was matched incorrectly.",
				testMatcher.match(TEST_FAVORITE_BY_ID), MoviesProvider.FAVORITE_BY_ID);
		Log.d(getClass().getSimpleName(), String.format("TEST_REMOVE_FAVORITE_BY_ID: %s MoviesProvider.REMOVE_FAVORITE_BY_ID: %s", TEST_REMOVE_FAVORITE_BY_ID, MoviesProvider.REMOVE_FAVORITE_BY_ID));
		assertEquals("Error: The LOCATION URI was matched incorrectly.",
				testMatcher.match(TEST_REMOVE_FAVORITE_BY_ID), MoviesProvider.REMOVE_FAVORITE_BY_ID);
	}
}
