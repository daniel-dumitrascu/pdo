package org.projects.centralpoint.Extern.Fetching;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 03-Jun-18.
 */
public class VideoData
{
    public VideoData()
    {
        genres = new ArrayList<String>();
    }

    public int getTmdbId() { return tmdb_id; }
    public void setTmdbId(int tmdb_id) { this.tmdb_id = tmdb_id; }

    public String getImdbId() { return imdb_id; }
    public void setImdbId(String imdb_id) { this.imdb_id = imdb_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public String getPosterPath() { return poster_path; }
    public void setPosterPath(String poster_path) { this.poster_path = poster_path; }

    public String getReleaseDate() { return release_date; }
    public void setReleaseDate(String release_date) { this.release_date = release_date; }

    public int getRevenue() { return revenue; }
    public void setRevenue(int revenue) { this.revenue = revenue; }

    public int getBudget() { return budget; }
    public void setBudget(int budget) { this.budget = budget; }

    public int getRuntime() { return runtime; }
    public void setRuntime(int runtime) { this.runtime = runtime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public float getVoteAverage() { return vote_average; }
    public void setVoteAverage(float vote_average) { this.vote_average = vote_average; }

    public int getVoteCount() { return vote_count; }
    public void setVoteCount(int vote_count) { this.vote_count = vote_count; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }

    public List<String> getCountries() { return countries; }
    public void setCountries(List<String> countries) { this.countries = countries; }

    public List<PersonData> getActors() { return actors; }
    public void setActors(List<PersonData> actors) { this.actors = actors; }

    private int tmdb_id;
    private String imdb_id;
    private String title;
    private String overview;
    private String poster_path;
    private String release_date;
    private int revenue;
    private int budget;
    private int runtime;
    private String status;
    private float vote_average;
    private int vote_count;
    private List<String> genres;
    private List<String> languages;
    private List<String> countries;
    private List<PersonData> actors;
}
