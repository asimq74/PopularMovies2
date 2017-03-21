package com.example.popularmovies.businessobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Movie Information Business Object
 * <p/>
 * Created by Asim Qureshi.
 */
public class MovieInfo {

	private String adult = "";
	private String backdropPath = "";
	private List<Integer> genreIds = new ArrayList<>();
	private int id = 0;
	private String originalLanguage = "";
	private String originalTitle = "";
	private String overview = "";
	private String popularity = "";
	private String posterPath = "";
	private String releaseDate = "";
	private String title = "";
	private String video = "";
	private String voteAverage = "";
	private String voteCount = "";

	public String getBackdropPath() {
		return backdropPath;
	}

	public List<Integer> getGenreIds() {
		return genreIds;
	}

	public int getId() {
		return id;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public String getOverview() {
		return overview;
	}

	public String getPopularity() {
		return popularity;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getTitle() {
		return title;
	}

	public String getVoteAverage() {
		return voteAverage;
	}

	public String getVoteCount() {
		return voteCount;
	}

	public void setAdult(String adult) {
		this.adult = adult;
	}

	public void setBackdropPath(String backdropPath) {
		this.backdropPath = backdropPath;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public void setPopularity(String popularity) {
		this.popularity = popularity;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public void setVoteAverage(String voteAverage) {
		this.voteAverage = voteAverage;
	}

	public void setVoteCount(String voteCount) {
		this.voteCount = voteCount;
	}

	@Override
	public String toString() {
		return "\nMovieInfo{" + "\n" +
				"posterPath='" + posterPath + '\'' + ",\n" +
				"adult='" + adult + '\'' + ",\n" +
				"overview='" + overview + '\'' + ",\n" +
				"releaseDate='" + releaseDate + '\'' + ",\n" +
				"genreIds=" + genreIds + ",\n" +
				"id=" + id + ",\n" +
				"originalTitle='" + originalTitle + '\'' + ",\n" +
				"originalLanguage='" + originalLanguage + '\'' + ",\n" +
				"title='" + title + '\'' + ",\n" +
				"backdropPath='" + backdropPath + '\'' + ",\n" +
				"popularity='" + popularity + '\'' + ",\n" +
				"voteCount='" + voteCount + '\'' + ",\n" +
				"video='" + video + '\'' + ",\n" +
				"voteAverage='" + voteAverage + '\'' +
				'}';
	}
}
