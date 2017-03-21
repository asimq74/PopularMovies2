package com.example.popularmovies.businessobjects;

/**
 * Movie Review Business Object
 * <p/>
 * Created by Asim Qureshi.
 */
public class Review {

	private String author = "";
	private String content = "";
	private int movieId = 0;
	private String reviewId = "";
	private String url = "";

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

}
