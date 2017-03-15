package com.example.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.data.MoviesContract.ReviewsEntry;

public class ReviewsAdapter extends CursorAdapter {

	public class ViewHolder extends RecyclerView.ViewHolder {

		public final TextView authorView;
		public final TextView contentView;
		public final TextView urlView;

		public ViewHolder(View view) {
			super(view);
			authorView = (TextView) view.findViewById(R.id.author);
			contentView = (TextView) view.findViewById(R.id.content);
			urlView = (TextView) view.findViewById(R.id.url);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + contentView.getText() + "'";
		}
	}

	public ReviewsAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.contentView.setText(cursor.getString(cursor.getColumnIndex(ReviewsEntry.COLUMN_CONTENT)));
		viewHolder.authorView.setText(cursor.getString(cursor.getColumnIndex(ReviewsEntry.COLUMN_AUTHOR)));
		viewHolder.urlView.setText(cursor.getString(cursor.getColumnIndex(ReviewsEntry.COLUMN_URL)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.fragment_review, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		view.setTag(viewHolder);
		return view;
	}

}
