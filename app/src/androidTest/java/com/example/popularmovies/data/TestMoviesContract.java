/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/*
		Students: This is NOT a complete test for the WeatherContract --- just for the functions
    that we expect you to write.
 */
public class TestMoviesContract extends AndroidTestCase {

	public void testBuildFavoritesById() {
		Uri favoritesByIdUri = MoviesContract.FavoritesEntry.buildFavoritesById(TestUtilities.TEST_ID);
		assertNotNull(String.format("Error: Null Uri returned.  You must fill-in %s in %s.", "buildFavoritesById", MoviesContract.class.getSimpleName()),
				favoritesByIdUri);
		assertEquals(String.format("Error: Movie ID not properly appended to the end of the Uri %s %s", TestUtilities.TEST_ID, favoritesByIdUri.getLastPathSegment()),
				String.format("%s", TestUtilities.TEST_ID), favoritesByIdUri.getLastPathSegment());
		assertEquals("Error: Favorites by ID Uri doesn't match our expected result", favoritesByIdUri.toString(), "content://com.example.popularmovies/favorites/" + TestUtilities.TEST_ID);
	}

	public void testBuildFavoritesMovies() {
		Uri favoriteMoviesUri = MoviesContract.MoviesEntry.buildFavoriteMovies();
		assertNotNull(String.format("Error: Null Uri returned.  You must fill-in %s in %s.", "buildFavoriteMovies", MoviesContract.class.getSimpleName()),
				favoriteMoviesUri);
		assertEquals(String.format("Error: favorites not properly appended to the end of the Uri %s %s", MoviesContract.PATH_FAVORITES, favoriteMoviesUri.getLastPathSegment()),
				String.format("%s", MoviesContract.PATH_FAVORITES), favoriteMoviesUri.getLastPathSegment());
		assertEquals("Error: Favorite Movies Uri doesn't match our expected result", favoriteMoviesUri.toString(), "content://com.example.popularmovies/movies/" + MoviesContract.PATH_FAVORITES);
	}

	public void testBuildMovieById() {
		Uri movieByIdUri = MoviesContract.MoviesEntry.buildMovieById(TestUtilities.TEST_ID);
		assertNotNull(String.format("Error: Null Uri returned.  You must fill-in %s in %s.", "buildMovieById", MoviesContract.class.getSimpleName()),
				movieByIdUri);
		assertEquals(String.format("Error: Movie ID not properly appended to the end of the Uri %s %s", TestUtilities.TEST_ID, movieByIdUri.getLastPathSegment()),
				String.format("%s", TestUtilities.TEST_ID), movieByIdUri.getLastPathSegment());
		assertEquals("Error: Movie by ID Uri doesn't match our expected result", movieByIdUri.toString(), "content://com.example.popularmovies/movies/" + TestUtilities.TEST_ID);
	}

	public void testBuildReviewsById() {
		Uri reviewsByIdUri = MoviesContract.ReviewsEntry.buildReviewsById(TestUtilities.TEST_ID);
		assertNotNull(String.format("Error: Null Uri returned.  You must fill-in %s in %s.", "buildReviewsById", MoviesContract.class.getSimpleName()),
				reviewsByIdUri);
		assertEquals(String.format("Error: Movie ID not properly appended to the end of the Uri %s %s", TestUtilities.TEST_ID, reviewsByIdUri.getLastPathSegment()),
				String.format("%s", TestUtilities.TEST_ID), reviewsByIdUri.getLastPathSegment());
		assertEquals("Error: Reviews by ID Uri doesn't match our expected result", reviewsByIdUri.toString(), "content://com.example.popularmovies/reviews/" + TestUtilities.TEST_ID);
	}

	public void testBuildVideosById() {
		Uri videosByIdUri = MoviesContract.VideosEntry.buildVideosById(TestUtilities.TEST_ID);
		assertNotNull(String.format("Error: Null Uri returned.  You must fill-in %s in %s.", "buildVideosById", MoviesContract.class.getSimpleName()),
				videosByIdUri);
		assertEquals(String.format("Error: Movie ID not properly appended to the end of the Uri %s %s", TestUtilities.TEST_ID, videosByIdUri.getLastPathSegment()),
				String.format("%s", TestUtilities.TEST_ID), videosByIdUri.getLastPathSegment());
		assertEquals("Error: Videos by ID Uri doesn't match our expected result", videosByIdUri.toString(), "content://com.example.popularmovies/videos/" + TestUtilities.TEST_ID);
	}

	public void testDeleteFavoritesById() {
		Uri deleteFavoritesByIdUri = MoviesContract.FavoritesEntry.removeFavoriteById(TestUtilities.TEST_ID);
		assertNotNull(String.format("Error: Null Uri returned.  You must fill-in %s in %s.", "deleteFavoritesByIdUri", MoviesContract.class.getSimpleName()),
				deleteFavoritesByIdUri);
		assertEquals(String.format("Error: remove not properly appended to the end of the Uri %s %s", TestUtilities.TEST_ID, deleteFavoritesByIdUri.getLastPathSegment()),
				String.format("%s", "remove"), deleteFavoritesByIdUri.getLastPathSegment());
		assertEquals(String.format("Error: Movie ID not properly appended to the second to last of the Uri %s %s",
				TestUtilities.TEST_ID, deleteFavoritesByIdUri.getPathSegments().get(deleteFavoritesByIdUri.getPathSegments().size() - 2)),
				String.format("%s", TestUtilities.TEST_ID), deleteFavoritesByIdUri.getPathSegments().get(deleteFavoritesByIdUri.getPathSegments().size() - 2));
		assertEquals("Error: Delete Favorites by ID Uri doesn't match our expected result", deleteFavoritesByIdUri.toString(), String.format("content://com.example.popularmovies/favorites/%s/remove", TestUtilities.TEST_ID));
	}
}
