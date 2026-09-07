package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    private String movieId;
    private String title;
    private String description;
    private int duration;
    private int releaseYear;
    private double rating;
    private int views;
    private int favoriteCount;
    private String director;
    private List<String> actors;

    public Movie() {
        this.actors = new ArrayList<>();
    }

    public Movie(String movieId, String title, String description, int duration, int releaseYear, double rating, String director, List<String> actors) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.views = 0;
        this.favoriteCount = 0;
        this.director = director;
        this.actors = actors != null ? actors : new ArrayList<>();
    }

    public String getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public int getReleaseYear() { return releaseYear; }
    public double getRating() { return rating; }
    public int getViews() { return views; }
    public int getFavoriteCount() { return favoriteCount; }
    public String getDirector() { return director; }
    public List<String> getActors() { return actors; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public void setDirector(String director) { this.director = director; }
    public void setActors(List<String> actors) { this.actors = actors; }

    public void increaseView() { this.views++; }
    public void increaseFavoriteCount() { this.favoriteCount++; }
    public void decreaseFavoriteCount() { if (this.favoriteCount > 0) this.favoriteCount--; }
    public void updateRating(double newRating) { this.rating = newRating; }
}