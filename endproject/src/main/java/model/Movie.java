package model;

import java.util.List;

public class Movie {
    private String movieId;
    private String title;
    private String description;
    private int duration;
    private int releaseYear;
    private String director;
    private List<String> actors;
    private double rating;
    private int views;
    private int favoriteCount;
    private int likeCount;
    private int dislikeCount;

    public Movie(String movieId, String title, String description, int duration, int releaseYear, double rating, String director, List<String> actors) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.director = director;
        this.actors = actors;
        this.rating = rating;
        this.views = 0;
        this.favoriteCount = 0;
        this.likeCount = 0;
        this.dislikeCount = 0;
    }

    public String getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public List<String> getActors() { return actors; }
    public void setActors(List<String> actors) { this.actors = actors; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getViews() { return views; }
    public void increaseView() { this.views++; }
    public int getFavoriteCount() { return favoriteCount; }
    public void increaseFavoriteCount() { this.favoriteCount++; }
    public void decreaseFavoriteCount() { this.favoriteCount--; }
    public int getLikeCount() { return likeCount; }
    public void increaseLike() { this.likeCount++; }
    public int getDislikeCount() { return dislikeCount; }
    public void increaseDislike() { this.dislikeCount++; }
}