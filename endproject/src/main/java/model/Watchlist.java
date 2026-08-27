/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class Watchlist implements Serializable {
    private static final long serialVersionUID = 1L;

    private String watchlistId;
    private String userId;
    private LocalDateTime createdAt;
    private List<WatchlistItem> items;

    public Watchlist(String watchlistId, String userId) {
        this.watchlistId = watchlistId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    public String getWatchlistId() { return watchlistId; }
    public String getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void addMovie(String movieId) {
        if (!isMovieInWatchlist(movieId)) {
            items.add(new WatchlistItem(this.watchlistId, movieId));
        }
    }

    public void removeMovie(String movieId) {
        items.removeIf(item -> item.getMovieId().equals(movieId));
    }

    public boolean isMovieInWatchlist(String movieId) {
        return items.stream().anyMatch(item -> item.getMovieId().equals(movieId));
    }

    public List<String> getMovies() {
        List<String> movieIds = new ArrayList<>();
        for (WatchlistItem item : items) {
            movieIds.add(item.getMovieId());
        }
        return movieIds;
    }
}
