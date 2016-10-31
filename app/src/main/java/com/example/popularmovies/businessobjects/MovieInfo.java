package com.example.popularmovies.businessobjects;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Movie Information Business Object
 * <p/>
 * Created by Asim Qureshi.
 */
public class MovieInfo implements Parcelable {
    private String posterPath = "";
    private String adult = "";
    private String overview = "";
    private String releaseDate = "";
    private List<Integer> genreIds = new ArrayList<>();
    private int id = 0;
    private String originalTitle = "";
    private String originalLanguage = "";
    private String title = "";
    private String backdropPath = "";
    private String popularity = "";
    private String voteCount = "";
    private String video = "";
    private String voteAverage = "";

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {

        public MovieInfo createFromParcel(Parcel in) {
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.posterPath = in.readString();
            movieInfo.adult = in.readString();
            movieInfo.overview = in.readString();
            movieInfo.releaseDate = in.readString();
            movieInfo.genreIds = in.readArrayList(Integer.class.getClassLoader());
            movieInfo.id = in.readInt();
            movieInfo.originalTitle = in.readString();
            movieInfo.originalLanguage = in.readString();
            movieInfo.title = in.readString();
            movieInfo.backdropPath = in.readString();
            movieInfo.popularity = in.readString();
            movieInfo.voteCount = in.readString();
            movieInfo.video = in.readString();
            movieInfo.voteAverage = in.readString();
            return movieInfo;
        }

        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(adult);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeList(genreIds);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeString(popularity);
        dest.writeString(voteCount);
        dest.writeString(video);
        dest.writeString(voteAverage);
    }


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Calendar getReleaseDateAsCalendar() {
        DateFormat df = new SimpleDateFormat("yyyy-dd-mm");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(df.parse(releaseDate));
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.toString(), e);
        }
        return cal;
    }

    public String getReleaseDateLongFormat() {
        Calendar calendar = getReleaseDateAsCalendar();
        SimpleDateFormat format = new SimpleDateFormat("MMMM D, yyyy");
        return format.format(calendar.getTime());
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
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
