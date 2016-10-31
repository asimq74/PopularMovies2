package com.example.popularmovies.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.businessobjects.MovieImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Async Task to list all movie images for a specific movie id.
 * <p/>
 * Created by Asim Qureshi.
 */
public class ListMovieImagesAsyncTask extends AsyncTask<Integer, Void, MovieImage> {

    final String TAG = ListMovieImagesAsyncTask.class.getSimpleName();
    private final ImageView imageView;
    private final Context context;

    public ListMovieImagesAsyncTask(Context context, ImageView imageView) {
        super();
        this.context = context;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(MovieImage movieImage) {
        final String imageUrlPath = "http://image.tmdb.org/t/p/w"
                + movieImage.getWidth()
                + movieImage.getFilePath()
                + "?api_key="
                + BuildConfig.THE_MOVIE_DB_API_KEY
                + "&language=en&include_image_language=en,null";
        Log.i(TAG, String.format("imageUrlPath=%s", imageUrlPath));
        Picasso.with(context).load(imageUrlPath).into(imageView);
    }

    @Override
    protected MovieImage doInBackground(Integer... params) {
        Integer id = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

// Will contain the raw JSON response as a string.
        String moviesJsonString;
        MovieImage movieImage = new MovieImage();

        try {
            // Construct the URL
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(id.toString())
                    .appendPath("images")
                    .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);
            final String urlString = builder.build().toString();
            URL url = new URL(urlString);
            // Create the request and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) Log.d(TAG, "Stream was empty");
            moviesJsonString = buffer.toString();
            movieImage = formatJson(moviesJsonString);
        } catch (Exception e) {
            Log.e(TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return movieImage;

    }

    protected MovieImage formatJson(String json) throws JSONException {
        // These are the names of the JSON objects that need to be extracted.
        final String ASPECT_RATIO = "aspect_ratio";
        final String FILE_PATH = "file_path";
        final String HEIGHT = "height";
        final String WIDTH = "width";
        final String BACKDROPS = "backdrops";

        JSONObject moviePosterJson = new JSONObject(json);
        JSONArray moviesImageArray = moviePosterJson.getJSONArray(BACKDROPS);

        List<MovieImage> movieImages = new ArrayList<>();
        for (int i = 0; i < moviesImageArray.length(); i++) {
            JSONObject moviesImageJSONObject = moviesImageArray.getJSONObject(i);
            MovieImage movieImage = new MovieImage();
            movieImage.setAspectRatio(moviesImageJSONObject.getLong(ASPECT_RATIO));
            movieImage.setFilePath(moviesImageJSONObject.getString(FILE_PATH));
            movieImage.setHeight(moviesImageJSONObject.getInt(HEIGHT));
            movieImage.setWidth(moviesImageJSONObject.getInt(WIDTH));
            movieImages.add(movieImage);
        }
        return movieImages.get(random(movieImages.size() - 1));
    }

    protected int random(int length) {
        Random rand = new Random();
        return rand.nextInt(length);
    }
}
