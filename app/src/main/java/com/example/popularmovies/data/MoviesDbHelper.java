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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.popularmovies.data.MoviesContract.FavoritesEntry;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

/**
 * Manages a local database for weather data.
 */
class MoviesDbHelper extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "movies.db";
	// If you change the database schema, you must increment the database version.
	private static final int DATABASE_VERSION = 8;

	MoviesDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {

		// Create a table to hold videos
		final String SQL_CREATE_VIDEOS_TABLE = "CREATE TABLE " + VideosEntry.TABLE_NAME + " (" +
				VideosEntry._ID + " TEXT PRIMARY KEY," +
				VideosEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
				VideosEntry.COLUMN_NAME + " TEXT NOT NULL, " +
				VideosEntry.COLUMN_SITE + " TEXT NOT NULL, " +
				VideosEntry.COLUMN_SIZE + " INTEGER NOT NULL, " +
				VideosEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
				VideosEntry.COLUMN_KEY + " TEXT NOT NULL, " +
				" FOREIGN KEY (" + VideosEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
				MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + "));";

		// Create a table to hold reviews
		final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
				ReviewsEntry._ID + " TEXT PRIMARY KEY," +
				ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
				ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
				ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
				ReviewsEntry.COLUMN_URL + " TEXT NOT NULL, " +
				" FOREIGN KEY (" + ReviewsEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
				MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + "));";

		// Create a table to hold favorites
		final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " (" +
				FavoritesEntry._ID + " TEXT PRIMARY KEY);";

		// Create a table to hold movies
		final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
				MoviesEntry._ID + " INTEGER PRIMARY KEY," +
				MoviesEntry.COLUMN_ADULT + " BOOLEAN NOT NULL, " +
				MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
				MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
				MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
				MoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
				MoviesEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL," +
				MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL," +
				MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL," +
				MoviesEntry.COLUMN_POPULARITY + " INTEGER NOT NULL," +
				MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL," +
				MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
				MoviesEntry.COLUMN_VIDEO + " BOOLEAN NOT NULL," +
				MoviesEntry.COLUMN_SEARCH_CRITERIA + " TEXT NOT NULL" +
				" );";

		sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
		sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideosEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
		onCreate(sqLiteDatabase);
	}
}
