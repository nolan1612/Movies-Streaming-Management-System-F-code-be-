/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nguyenhoangminhnhat
 */
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;

    private String movieId;
    private String title;
    private String description;
    private int duration; // tính bằng phút
    private int releaseYear;
    private double rating; // 0 - 10
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

    public void increaseView() { this.views++; }
    public void increaseFavoriteCount() { this.favoriteCount++; }
    public void decreaseFavoriteCount() { if (this.favoriteCount > 0) this.favoriteCount--; }
    public void updateRating(double newRating) { this.rating = newRating; }
}
