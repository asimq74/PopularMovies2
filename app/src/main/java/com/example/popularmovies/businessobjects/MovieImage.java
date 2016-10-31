package com.example.popularmovies.businessobjects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movie Image Business Object
 * <p/>
 * Created by Asim Qureshi.
 */
public class MovieImage implements Parcelable {

    private float aspectRatio = 0.0f;
    private String filePath = "";
    private int height = 0;
    private int width = 0;
    public static final Parcelable.Creator<MovieImage> CREATOR = new Parcelable.Creator<MovieImage>() {

        public MovieImage createFromParcel(Parcel in) {
            MovieImage movieImage = new MovieImage();
            movieImage.aspectRatio = in.readFloat();
            movieImage.filePath = in.readString();
            movieImage.height = in.readInt();
            movieImage.width = in.readInt();
            return movieImage;
        }

        public MovieImage[] newArray(int size) {
            return new MovieImage[size];
        }
    };

    @Override
    public String toString() {
        return "\nMovieImage{" + "\n" +
                "aspectRatio='" + aspectRatio + '\'' + ",\n" +
                "filePath='" + filePath + '\'' + ",\n" +
                "height='" + height + '\'' + ",\n" +
                "width='" + width + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.aspectRatio);
        dest.writeString(this.filePath);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
