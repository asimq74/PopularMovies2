package com.example.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

/**
 * Content Provider for movies
 * <p>
 * Created by Asim Qureshi
 */

public class MoviesProvider extends ContentProvider {

    static final int MOVIES = 100;
    static final int MOVIE_BY_ID = 101;
    static final int MOVIE_REVIEWS = 102;
    static final int MOVIE_TRAILERS = 300;
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder movieTrailersByMovieIdQueryBuilder;
    private static final SQLiteQueryBuilder movieReviewsByMovieIdQueryBuilder;

    static {
        movieTrailersByMovieIdQueryBuilder = new SQLiteQueryBuilder();
        movieReviewsByMovieIdQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        movieTrailersByMovieIdQueryBuilder.setTables(
                MoviesContract.MoviesEntry.TABLE_NAME + " INNER JOIN " +
                        MoviesContract.VideosEntry.TABLE_NAME +
                        " ON " + MoviesContract.MoviesEntry.TABLE_NAME +
                        "." + MoviesEntry._ID +
                        " = " + MoviesContract.VideosEntry.TABLE_NAME +
                        "." + VideosEntry.COLUMN_MOVIE_ID);

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        movieReviewsByMovieIdQueryBuilder.setTables(
                MoviesContract.MoviesEntry.TABLE_NAME + " INNER JOIN " +
                        MoviesContract.ReviewsEntry.TABLE_NAME +
                        " ON " + MoviesContract.MoviesEntry.TABLE_NAME +
                        "." + MoviesEntry._ID +
                        " = " + MoviesContract.ReviewsEntry.TABLE_NAME +
                        "." + MoviesContract.ReviewsEntry.COLUMN_MOVIE_ID);
    }

    private MoviesDbHelper mOpenHelper;

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/*", MOVIE_BY_ID);
        matcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/*", MOVIE_REVIEWS);
        matcher.addURI(authority, MoviesContract.PATH_VIDEOS + "/*", MOVIE_TRAILERS);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
