package com.example.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by U1C306 on 1/13/2017.
 */

public class MovieInfoAdapter extends CursorAdapter {

	public static class ViewHolder {

		public final ImageView moviePosterImageView;

		public ViewHolder(View view) {
			moviePosterImageView = (ImageView) view.findViewById(R.id.movie_poster);
		}
	}

	public MovieInfoAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		// Populate the data into the template view using the data object
		Picasso.with(context).load("http://image.tmdb.org/t/p/w500/"
				+ cursor.getString(MoviesGridFragment.COL_POSTER_PATH)
				+ "?api_key="
				+ BuildConfig.THE_MOVIE_DB_API_KEY
				+ "&language=en&include_image_language=en,null").into(viewHolder.moviePosterImageView);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.movie_poster_item, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		view.setTag(viewHolder);
		return view;
	}

}
