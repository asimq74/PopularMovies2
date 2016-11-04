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

import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

/**
 * Manages a local database for weather data.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movies.db";
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold videos
        final String SQL_CREATE_VIDEOS_TABLE = "CREATE TABLE " + VideosEntry.TABLE_NAME + " (" +
                VideosEntry._ID + " TEXT PRIMARY KEY," +
                VideosEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                VideosEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                VideosEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                VideosEntry.COLUMN_SIZE + " INTEGER NOT NULL, " +
                VideosEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                VideosEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + VideosEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + ");";

        // Create a table to hold videos
        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +
                ReviewsEntry._ID + " TEXT PRIMARY KEY," +
                ReviewsEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                ReviewsEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_URL + " TEXT NOT NULL, " +
                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + ReviewsEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry._ID + ");";

		final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
				// Why AutoIncrement here, and not above?
				// Unique keys will be auto-generated in either case.  But for weather
				// forecasting, it's reasonable to assume the user will want information
				// for a certain date and all dates *following*, so the forecast data
				// should be sorted accordingly.
				MoviesEntry._ID + " INTEGER PRIMARY KEY," +

				// the ID of the location entry associated with this weather data
				MoviesEntry.COLUMN_ADULT + " TEXT NOT NULL, " +
				MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
				MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
				MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
				MoviesEntry.COLUMN_ORIGINAL_TITLE  + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_ORIGINAL_LANGUAGE  + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_TITLE  + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_BACKDROP_PATH  + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_POPULARITY  + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_VOTE_COUNT  + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_VOTE_AVERAGE  + " TEXT NOT NULL," +
                MoviesEntry.COLUMN_VIDEO  + " TEXT NOT NULL" +
                " );";

		sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideosEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
