package com.example.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.businessobjects.MovieConstants;
import com.example.popularmovies.businessobjects.MovieInfo;
import com.example.popularmovies.data.MoviesContract;
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
import java.util.Vector;

/**
 * A fragment representing a list of Movie Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Created by Asim Qureshi.
 */
public class MoviesGridFragment extends Fragment implements MovieConstants {


    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private MovieInfoAdapter movieInfoAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MoviesGridFragment() {
    }

    public static MoviesGridFragment newInstance() {
        return new MoviesGridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movieinfo, container, false);

        List<MovieInfo> movieInfoList = new ArrayList<>();
// Create the movieInfoAdapter to convert the array to views
        movieInfoAdapter = new MovieInfoAdapter(getActivity(), movieInfoList);
// Attach the movieInfoAdapter to a ListView
        GridView gridView = (GridView) view.findViewById(android.R.id.list);
        gridView.setAdapter(movieInfoAdapter);
        gridView.setClickable(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfo movieInfo = movieInfoAdapter.getItem(position);
                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(MOVIE_INFO_PARCELABLE_KEY, movieInfo);
                movieDetailIntent.putExtras(mBundle);
                startActivity(movieDetailIntent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    protected void updateMovies() {
        FetchMoviesTask updateMoviesTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String criteria = prefs.getString(getString(R.string.pref_search_criteria_key), getString(R.string.pref_search_criteria_default));
        updateMoviesTask.execute(criteria);
    }

    public class MovieInfoAdapter extends ArrayAdapter<MovieInfo> {

        public MovieInfoAdapter(Context context, List<MovieInfo> movieInfos) {
            super(context, 0, movieInfos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final MovieInfo movieInfo = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster_item, parent, false);
            }
            // Lookup view for data population
            ImageView moviePosterImageView = (ImageView) convertView.findViewById(R.id.movie_poster);
            TextView movieTitleView = (TextView) convertView.findViewById(R.id.movie_title);
            // Populate the data into the template view using the data object
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500/"
                    + movieInfo.getPosterPath()
                    + "?api_key="
                    + BuildConfig.THE_MOVIE_DB_API_KEY
                    + "&language=en&include_image_language=en,null").into(moviePosterImageView);
            movieTitleView.setText(movieInfo.getTitle());
            return convertView;
        }
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<MovieInfo>> {

        final String TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPostExecute(List<MovieInfo> movieInfos) {
            super.onPostExecute(movieInfos);
            if (movieInfos != null) {
                movieInfoAdapter.clear();
                for (MovieInfo movieInfo : movieInfos) {
                    movieInfoAdapter.add(movieInfo);
                }
            }
        }


        protected List<MovieInfo> formatJson(String json) throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String POSTER_PATH = "poster_path";
            final String ADULT = "adult";
            final String OVERVIEW = "overview";
            final String RELEASE_DATE = "release_date";
            final String GENRE_IDS = "genre_ids";
            final String ID = "id";
            final String ORIGINAL_TITLE = "original_title";
            final String ORIGINAL_LANGUAGE = "original_language";
            final String TITLE = "title";
            final String BACKDROP_PATH = "backdrop_path";
            final String POPULARITY = "popularity";
            final String VOTE_COUNT = "vote_count";
            final String VIDEO = "video";
            final String VOTE_AVERAGE = "vote_average";

            JSONObject forecastJson = new JSONObject(json);
            JSONArray moviesArray = forecastJson.getJSONArray(RESULTS);
            List<MovieInfo> movies = new ArrayList<>();
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieInfoJsonObject = moviesArray.getJSONObject(i);
                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setPosterPath(movieInfoJsonObject.getString(POSTER_PATH));
                movieInfo.setAdult(movieInfoJsonObject.getString(ADULT));
                movieInfo.setOverview(movieInfoJsonObject.getString(OVERVIEW));
                movieInfo.setReleaseDate(movieInfoJsonObject.getString(RELEASE_DATE));
                movieInfo.setId(movieInfoJsonObject.getInt(ID));
                movieInfo.setOriginalTitle(movieInfoJsonObject.getString(ORIGINAL_TITLE));
                movieInfo.setOriginalLanguage(movieInfoJsonObject.getString(ORIGINAL_LANGUAGE));
                movieInfo.setTitle(movieInfoJsonObject.getString(TITLE));
                movieInfo.setBackdropPath(movieInfoJsonObject.getString(BACKDROP_PATH));
                movieInfo.setPopularity(movieInfoJsonObject.getString(POPULARITY));
                movieInfo.setVoteCount(movieInfoJsonObject.getString(VOTE_COUNT));
                movieInfo.setVideo(movieInfoJsonObject.getString(VIDEO));
                movieInfo.setVoteAverage(movieInfoJsonObject.getString(VOTE_AVERAGE));
                populateGenreIds(GENRE_IDS, movieInfoJsonObject, movieInfo);
                movies.add(movieInfo);
            }
            return movies;
        }

        protected void populateGenreIds(String GENRE_IDS, JSONObject movieInfoJsonObject, MovieInfo movieInfo) throws JSONException {
            JSONArray jsonArray = movieInfoJsonObject.getJSONArray(GENRE_IDS);
            if (jsonArray != null) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    movieInfo.getGenreIds().add(Integer.valueOf(jsonArray.get(j).toString()));
                }
            }
        }

        @Override
        protected List<MovieInfo> doInBackground(String... params) {
            String criteria = params[0];
            List<MovieInfo> movieInfos = new ArrayList<>();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonString;

            try {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(criteria)
                        .appendQueryParameter("sort_by", "popularity.desc")
                        .appendQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY);

                final String builtURLString = builder.build().toString();
                Log.i(TAG, String.format("builtURLString: %s", builtURLString));
                URL url = new URL(builtURLString);
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
                if (buffer.length() == 0) {
                    Log.d(TAG, "Stream was empty");
                }
                moviesJsonString = buffer.toString();
                movieInfos = formatJson(moviesJsonString);
                Vector<ContentValues> cVVector = new Vector<ContentValues>(movieInfos.size());
                for (MovieInfo movieInfo : movieInfos) {
                    ContentValues movieValues = new ContentValues();

                    movieValues.put(MoviesContract.MoviesEntry._ID, movieInfo.getId());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_ADULT, 0);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_FAVORITE, 0);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_BACKDROP_PATH, movieInfo.getBackdropPath());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_LANGUAGE, movieInfo.getOriginalLanguage());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_ORIGINAL_TITLE, movieInfo.getOriginalTitle());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, movieInfo.getOverview());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_POPULARITY, movieInfo.getPopularity());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, movieInfo.getPosterPath());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, movieInfo.getReleaseDate());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, movieInfo.getTitle());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_VIDEO, 0);
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movieInfo.getVoteAverage());
                    movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_COUNT, movieInfo.getVoteCount());

                    cVVector.add(movieValues);
                }

                int inserted = 0;
                // add to database
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = getActivity().getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, cvArray);
                }
                Log.d(TAG, "FetchMoviesTask Complete. " + inserted + " Inserted");

            } catch (JSONException e) {
                Log.e(TAG, String.format("JSONException Error e: %s", e.getMessage()), e);

            } catch (IOException e) {
                Log.e(TAG, String.format("IOException Error e: %s", e.getMessage()), e);
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
            return movieInfos;
        }
    }
}
