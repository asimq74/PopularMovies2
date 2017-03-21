package com.example.popularmovies.sync;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.example.popularmovies.R;
import com.example.popularmovies.Utility;
import com.example.popularmovies.businessobjects.MovieInfo;
import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.service.MovieInfoRetriever;
import com.example.popularmovies.service.MovieReviewsRetriever;
import com.example.popularmovies.service.MovieVideosRetriever;
import com.example.popularmovies.service.RetrieveMovieInfoApi;

public class MoviesSyncAdapter extends AbstractThreadedSyncAdapter {

	/**
	 * Helper method to get the fake account to be used with SyncAdapter, or make a new one
	 * if the fake account doesn't exist yet.  If we make a new account, we call the
	 * onAccountCreated method so we can initialize things.
	 *
	 * @param context The context used to access the account service
	 * @return a fake account.
	 */
	private static Account getSyncAccount(Context context) {
		// Get an instance of the Android account manager
		AccountManager accountManager =
				(AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

		// Create the account type and default account
		Account newAccount = new Account(
				context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

		// If the password doesn't exist, the account doesn't exist
		if (null == accountManager.getPassword(newAccount)) {

        /*
				 * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
			if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
				return null;
			}
						/*
						 * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

		}
		return newAccount;
	}

	/**
	 * Helper method to have the sync adapter sync immediately
	 * @param context The context used to access the account service
	 */
	public static void syncImmediately(Context context) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		ContentResolver.requestSync(getSyncAccount(context),
				context.getString(R.string.content_authority), bundle);
	}

	private final String TAG = MoviesSyncAdapter.class.getSimpleName();

	MoviesSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
		Log.d(TAG, "Starting sync");
		String criteria = Utility.getPreferredCriteria(getContext());
		if (MoviesContract.FAVORITES.equals(criteria)) {
			return;
		}
		RetrieveMovieInfoApi<MovieInfo, List<String>> retrieveMoviesHandler = new MovieInfoRetriever(getContext());
		List<String> movieIds = retrieveMoviesHandler.retrieveMovieInformation(criteria);
		MovieReviewsRetriever reviewsHandler = new MovieReviewsRetriever(getContext());
		MovieVideosRetriever videosHandler = new MovieVideosRetriever(getContext());
		for (String movieId : movieIds) {
			List<String> reviewIds = reviewsHandler.retrieveMovieInformation(movieId);
			List<String> videoIds = videosHandler.retrieveMovieInformation(movieId);
			Log.d(TAG, String.format("reviewIds: %s", reviewIds));
			Log.d(TAG, String.format("videoIds: %s", videoIds));
		}
	}
}