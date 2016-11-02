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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class MoviesContract {

	/* Inner class that defines the table contents of the weather table */
	public static final class MoviesEntry implements BaseColumns {

		public static final String COLUMN_ADULT = "adult";
		public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
		public static final String COLUMN_GENRE_IDS = "genre_ids";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
		public static final String COLUMN_ORIGINAL_TITLE = "original_title";
		public static final String COLUMN_OVERVIEW = "overview";
		public static final String COLUMN_POPULARITY = "popularity";
		public static final String COLUMN_POSTER_PATH = "poster_path";
		public static final String COLUMN_RELEASE_DATE = "release_date";
		public static final String COLUMN_RESULTS = "results";
		public static final String COLUMN_TITLE = "title";
		public static final String COLUMN_VIDEO = "video";
		public static final String COLUMN_VOTE_AVERAGE = "vote_average";
		public static final String COLUMN_VOTE_COUNT = "vote_count";
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
		public static final Uri CONTENT_URI =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
		public static final String TABLE_NAME = "movies";

//		public static Uri buildWeatherLocationWithDate(String locationSetting, long date) {
//			return CONTENT_URI.buildUpon().appendPath(locationSetting)
//					.appendPath(Long.toString(normalizeDate(date))).build();
//		}
//
//		public static Uri buildWeatherLocationWithStartDate(
//				String locationSetting, long startDate) {
//			long normalizedDate = normalizeDate(startDate);
//			return CONTENT_URI.buildUpon().appendPath(locationSetting)
//					.appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
//		}

	}

	/* Inner class that defines the table contents of the videos table */
	public static final class ReviewsEntry implements BaseColumns {

		public static final String COLUMN_AUTHOR = "author";
		public static final String COLUMN_CONTENT = "content";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_MOVIE_ID = "movie_id";
		public static final String COLUMN_URL = "url";
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
		public static final Uri CONTENT_URI =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();
		// Table name
		public static final String TABLE_NAME = "reviews";

		public static Uri buildReviewsUri(long movieId) {
			return ContentUris.withAppendedId(CONTENT_URI, movieId);
		}
	}

	/* Inner class that defines the table contents of the videos table */
	public static final class VideosEntry implements BaseColumns {

		public static final String COLUMN_KEY = "key";
		public static final String COLUMN_MOVIE_ID = "movie_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SITE = "site";
		public static final String COLUMN_SIZE = "size";
		public static final String COLUMN_TYPE = "type";
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;
		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEOS;
		public static final Uri CONTENT_URI =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEOS).build();
		// Table name
		public static final String TABLE_NAME = "videos";

		public static Uri buildVideosUri(long movieId) {
			return ContentUris.withAppendedId(CONTENT_URI, movieId);
		}
	}

	// The "Content authority" is a name for the entire content provider, similar to the
	// relationship between a domain name and its website.  A convenient string to use for the
	// content authority is the package name for the app, which is guaranteed to be unique on the
	// device.
	public static final String CONTENT_AUTHORITY = "com.example.popularmovies";
	// Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
	// the content provider.
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	// Possible paths (appended to base content URI for possible URI's)
	// For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
	// looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
	// as the ContentProvider hasn't been given any information on what to do with "givemeroot".
	// At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
	public static final String PATH_MOVIES = "movies";
	public static final String PATH_REVIEWS = "reviews";
	public static final String PATH_VIDEOS = "videos";

}
