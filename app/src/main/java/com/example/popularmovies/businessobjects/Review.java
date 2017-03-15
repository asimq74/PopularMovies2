package com.example.popularmovies.businessobjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie Information Business Object
 * <p/>
 * Created by Asim Qureshi.
 */
public class Review implements Parcelable {

	public Review() {
	}

	public static final Creator<Review> CREATOR = new Creator<Review>() {

		public Review createFromParcel(Parcel in) {
			Review review = new Review();
			review.movieId = in.readInt();
			review.author = in.readString();
			review.content = in.readString();
			review.url = in.readString();
			review.reviewId = in.readString();
			return review;
		}

		public Review[] newArray(int size) {
			return new Review[size];
		}
	};
	private String author = "";
	private String content = "";
	private int movieId = 0;
	private String reviewId = "";
	private String url = "";

	@Override
	public int describeContents() {
		return 0;
	}

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public int getMovieId() {
		return movieId;
	}

	public String getReviewId() {
		return reviewId;
	}

	public String getUrl() {
		return url;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "\nReview {" + "\n" +
				"movieId='" + movieId + '\'' + ",\n" +
				"author='" + author + '\'' + ",\n" +
				"content='" + content + '\'' + ",\n" +
				"url='" + url + '\'' + ",\n" +
				"reviewId=" + reviewId +
				'}';
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(movieId);
		dest.writeString(author);
		dest.writeString(content);
		dest.writeString(url);
		dest.writeString(reviewId);
	}

}
