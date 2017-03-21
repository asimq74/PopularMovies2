package com.example.popularmovies.businessobjects;

/**
 * Movie Video Business Object
 * <p/>
 * Created by Asim Qureshi.
 */
public class Video {

	private String key = "";
	private int movieId = 0;
	private String name = "";
	private String site = "";
	private int size = 0;
	private String type = "";
	private String videoId = "";

	public String getKey() {
		return key;
	}

	public int getMovieId() {
		return movieId;
	}

	public String getName() {
		return name;
	}

	public String getSite() {
		return site;
	}

	public int getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	@Override
	public String toString() {
		return "\nReview {" + "\n" +
				"movieId='" + movieId + '\'' + ",\n" +
				"key='" + key + '\'' + ",\n" +
				"site='" + site + '\'' + ",\n" +
				"type='" + type + '\'' + ",\n" +
				"videoId='" + videoId + '\'' + ",\n" +
				"size='" + size + '\'' + ",\n" +
				"name=" + name +
				'}';
	}

}
