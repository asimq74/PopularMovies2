package com.example.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.popularmovies.R;
import com.example.popularmovies.data.MoviesContract.FavoritesEntry;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;
import com.example.popularmovies.data.MoviesContract.ReviewsEntry;
import com.example.popularmovies.data.MoviesContract.VideosEntry;

/**
 * Content Provider for movies
 * <p>
 * Created by Asim Qureshi
 */

public class MoviesProvider extends ContentProvider {

	static final int FAVORITES = 303;
	static final int FAVORITE_BY_ID = 304;
	static final int MOVIES = 100;
	static final int MOVIE_BY_ID = 101;
	static final int MOVIE_REVIEWS = 102;
	static final int MOVIE_TRAILERS = 300;
	static final int REMOVE_FAVORITE_BY_ID = 305;
	static final int REVIEWS = 301;
	static final int TRAILERS = 302;
	static final int FAVORITE_MOVIES = 306;
	static final int MOST_POPULAR_MOVIES = 307;
	static final int HIGHEST_RATED_MOVIES = 308;

	private static final SQLiteQueryBuilder favoriteMoviesQueryBuilder;
	//location.location_setting = ? AND date = ?
	public static final String favoritesMovieIdSelection =
			FavoritesEntry.TABLE_NAME + "." + FavoritesEntry._ID + " = ?";
	public static final String moviesIdSelection =
			MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID + " = ?";
	public static final String reviewsIdSelection =
			ReviewsEntry.TABLE_NAME + "." + ReviewsEntry.COLUMN_MOVIE_ID + " = ?";
	private static final SQLiteQueryBuilder favoritesMovieIdSelectionQueryBuilder;
	private static final SQLiteQueryBuilder favoritesQueryBuilder;
	private static final SQLiteQueryBuilder movieReviewsByMovieIdQueryBuilder;
	private static final SQLiteQueryBuilder movieTrailersByMovieIdQueryBuilder;

	public static final String moviesSearchCriteriaSelection =
			MoviesEntry.TABLE_NAME +
					"." + MoviesEntry.COLUMN_SEARCH_CRITERIA + " = ? ";
	public static final String popularitySortOrder = MoviesEntry.COLUMN_POPULARITY + " DESC";
	public static final String ratingSortOrder = MoviesEntry.COLUMN_VOTE_AVERAGE + " DESC";
	private static final SQLiteQueryBuilder moviesQueryBuilder;
	private static final SQLiteQueryBuilder reviewsQueryBuilder;
	// The URI Matcher used by this content provider.
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private static final SQLiteQueryBuilder trailersQueryBuilder;

	static {
		movieTrailersByMovieIdQueryBuilder = new SQLiteQueryBuilder();
		movieReviewsByMovieIdQueryBuilder = new SQLiteQueryBuilder();
		moviesQueryBuilder = new SQLiteQueryBuilder();
		reviewsQueryBuilder = new SQLiteQueryBuilder();
		trailersQueryBuilder = new SQLiteQueryBuilder();
		favoritesQueryBuilder = new SQLiteQueryBuilder();
		favoritesMovieIdSelectionQueryBuilder = new SQLiteQueryBuilder();
		favoriteMoviesQueryBuilder = new SQLiteQueryBuilder();

		favoriteMoviesQueryBuilder.setTables(
				MoviesEntry.TABLE_NAME + " INNER JOIN " + FavoritesEntry.TABLE_NAME +
						" ON " + MoviesEntry.TABLE_NAME + "." + MoviesEntry._ID + " = " + FavoritesEntry.TABLE_NAME + "." + FavoritesEntry._ID);


		moviesQueryBuilder.setTables(
				MoviesContract.MoviesEntry.TABLE_NAME);

		trailersQueryBuilder.setTables(VideosEntry.TABLE_NAME);

		reviewsQueryBuilder.setTables(ReviewsEntry.TABLE_NAME);

		favoritesQueryBuilder.setTables(FavoritesEntry.TABLE_NAME);

		favoritesMovieIdSelectionQueryBuilder.setTables(FavoritesEntry.TABLE_NAME);

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
		matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_FAVORITES, FAVORITE_MOVIES);
		matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_HIGHEST_RATED, HIGHEST_RATED_MOVIES);
		matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/" + MoviesContract.PATH_MOST_POPULAR, MOST_POPULAR_MOVIES);
		matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/*", MOVIE_BY_ID);
		matcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/*", MOVIE_REVIEWS);
		matcher.addURI(authority, MoviesContract.PATH_VIDEOS + "/*", MOVIE_TRAILERS);
		matcher.addURI(authority, MoviesContract.PATH_REVIEWS, REVIEWS);
		matcher.addURI(authority, MoviesContract.PATH_VIDEOS, TRAILERS);
		matcher.addURI(authority, MoviesContract.PATH_FAVORITES, FAVORITES);
		matcher.addURI(authority, MoviesContract.PATH_FAVORITES + "/*", FAVORITE_BY_ID);
		matcher.addURI(authority, MoviesContract.PATH_FAVORITES + "/*/" + MoviesContract.REMOVE, REMOVE_FAVORITE_BY_ID);
		return matcher;
	}

	private MoviesDbHelper mOpenHelper;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int rowsDeleted;
		// this makes delete all rows return the number of rows deleted
		if (null == selection) selection = "1";
		Log.i(getClass().getSimpleName(), "match: " +  match);
		switch (match) {
			case MOVIES:
				rowsDeleted = db.delete(
						MoviesEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case TRAILERS:
				rowsDeleted = db.delete(
						VideosEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case REVIEWS:
				rowsDeleted = db.delete(
						ReviewsEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case FAVORITES:
				rowsDeleted = db.delete(
						FavoritesEntry.TABLE_NAME, selection, selectionArgs);
				break;
			case REMOVE_FAVORITE_BY_ID:
				rowsDeleted = db.delete(
						FavoritesEntry.TABLE_NAME, favoritesMovieIdSelection, new String[]{uri.getPathSegments().get(uri.getPathSegments().size() - 2)});
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		// Because a null deletes all rows
		if (rowsDeleted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsDeleted;
	}

	@NonNull
	private String formatFailedToInsertMessage(Uri uri) {
		return formatString(R.string.failed_to_insert_or_replace, uri);
	}

	@NonNull
	private String formatString(int id, Object... formatArgs) {
		return getContext().getResources().getString(id, formatArgs);
	}

	@NonNull
	private String formatUnknownUriMessage(Uri uri) {
		return formatString(R.string.unknown_uri, uri);
	}

	private Cursor getFavoriteById(Uri uri) {
		return favoritesMovieIdSelectionQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				null,
				favoritesMovieIdSelection,
				new String[]{uri.getLastPathSegment()},
				null,
				null,
				null
		);
	}

	private Cursor getMovieById(Uri uri, String[] projection) {
		return moviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				projection,
				moviesIdSelection,
				new String[]{uri.getLastPathSegment()},
				null,
				null,
				null
		);
	}


	private Cursor getFavorites() {
		return favoritesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				null,
				null,
				null,
				null,
				null,
				null
		);
	}

	private Cursor getFavoriteMovies() {
		return favoriteMoviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				null,
				null,
				null,
				null,
				null,
				null
		);
	}


	private Cursor getMostPopularMovies() {
		return moviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				null,
				moviesSearchCriteriaSelection,
				new String[]{MoviesContract.POPULAR},
				null,
				null,
				popularitySortOrder
		);
	}

	private Cursor getHighestRatedMovies() {
		return moviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				null,
				moviesSearchCriteriaSelection,
				new String[]{MoviesContract.TOP_RATED},
				null,
				null,
				ratingSortOrder
		);
	}

	private Cursor getMovies() {
		return moviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				null,
				null,
				null,
				null,
				null,
				null
		);
	}

	private Cursor getReviews(Uri uri) {
		return reviewsQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				null,
				reviewsIdSelection,
				new String[]{uri.getLastPathSegment()},
				null,
				null,
				null
		);
	}

	@Nullable
	@Override
	public String getType(Uri uri) {
		// Use the Uri Matcher to determine what kind of URI this is.
		final int match = sUriMatcher.match(uri);

		switch (match) {
			// Student: Uncomment and fill out these two cases
			case MOVIES:
				return MoviesEntry.CONTENT_TYPE;
			case MOVIE_BY_ID:
				return MoviesEntry.CONTENT_ITEM_TYPE;
			case MOVIE_REVIEWS:
				return ReviewsEntry.CONTENT_TYPE;
			case MOVIE_TRAILERS:
				return VideosEntry.CONTENT_TYPE;
			case REVIEWS:
				return ReviewsEntry.CONTENT_TYPE;
			case TRAILERS:
				return VideosEntry.CONTENT_TYPE;
			case FAVORITES:
				return FavoritesEntry.CONTENT_TYPE;
			case FAVORITE_MOVIES:
				return MoviesEntry.CONTENT_TYPE;
			case MOST_POPULAR_MOVIES:
				return MoviesEntry.CONTENT_TYPE;
			case HIGHEST_RATED_MOVIES:
				return MoviesEntry.CONTENT_TYPE;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	private Cursor getVideos() {
		return trailersQueryBuilder.query(mOpenHelper.getReadableDatabase(),
				null,
				null,
				null,
				null,
				null,
				null
		);
	}

	@Nullable
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		Uri returnUri;
		switch (match) {
			case MOVIES: {
				long _id = db.replace(MoviesEntry.TABLE_NAME, null, values);
				if (_id > 0)
					returnUri = MoviesEntry.buildMovieById(_id);
				else
					throw new android.database.SQLException(formatFailedToInsertMessage(uri));
				break;
			}
			case TRAILERS: {
				long _id = db.replace(VideosEntry.TABLE_NAME, null, values);
				if (_id > 0)
					returnUri = VideosEntry.buildVideosById(_id);
				else
					throw new android.database.SQLException(formatFailedToInsertMessage(uri));
				break;
			}
			case REVIEWS: {
				long _id = db.replace(ReviewsEntry.TABLE_NAME, null, values);
				if (_id > 0)
					returnUri = ReviewsEntry.buildReviewsById(_id);
				else
					throw new android.database.SQLException(formatFailedToInsertMessage(uri));
				break;
			}
			case FAVORITES: {
				long _id = db.replace(FavoritesEntry.TABLE_NAME, null, values);
				if (_id > 0)
					returnUri = FavoritesEntry.buildFavoritesById(_id);
				else
					throw new android.database.SQLException(formatFailedToInsertMessage(uri));
				break;
			}
			case FAVORITE_BY_ID: {
				long _id = db.replace(FavoritesEntry.TABLE_NAME, null, values);
				if (_id > 0) {
					Log.i(getClass().getSimpleName(), String.format("FavoritesEntry.buildFavoritesById(_id) %s", FavoritesEntry.buildFavoritesById(_id)));
					returnUri = FavoritesEntry.buildFavoritesById(_id);
				}
				else
					throw new android.database.SQLException(formatFailedToInsertMessage(uri));
				break;
			}
			default:
				throw new UnsupportedOperationException(formatUnknownUriMessage(uri));
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new MoviesDbHelper(getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor retCursor;
		switch (sUriMatcher.match(uri)) {
			case MOVIES: {
				retCursor = getMovies();
				break;
			}
			case REVIEWS: {
				retCursor = getReviews(uri);
				break;
			}
			case MOVIE_REVIEWS: {
				retCursor = getReviews(uri);
				break;
			}
			case TRAILERS: {
				retCursor = getVideos();
				break;
			}
			case FAVORITES: {
				retCursor = getFavorites();
				break;
			}
			case FAVORITE_BY_ID: {
				retCursor = getFavoriteById(uri);
				break;
			}
			case FAVORITE_MOVIES: {
				retCursor = getFavoriteMovies();
				break;
			}
			case MOST_POPULAR_MOVIES: {
				retCursor = getMostPopularMovies();
				break;
			}
			case HIGHEST_RATED_MOVIES: {
				retCursor = getHighestRatedMovies();
				break;
			}
			case MOVIE_BY_ID: {
				retCursor = getMovieById(uri, projection);
				break;
			}
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retCursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int rowsUpdated;
		// this makes delete all rows return the number of rows deleted
		if (null == selection) selection = "1";
		switch (match) {
			case MOVIES:
				rowsUpdated = db.update(MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
				break;
			case TRAILERS:
				rowsUpdated = db.update(VideosEntry.TABLE_NAME, values, selection, selectionArgs);
				break;
			case REVIEWS:
				rowsUpdated = db.update(ReviewsEntry.TABLE_NAME, values, selection, selectionArgs);
				break;
			case FAVORITES:
				rowsUpdated = db.update(FavoritesEntry.TABLE_NAME, values, selection, selectionArgs);
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		// Because a null deletes all rows
		if (rowsUpdated != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsUpdated;
	}
}
