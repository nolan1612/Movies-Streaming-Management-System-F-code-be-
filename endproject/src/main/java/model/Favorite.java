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
public class Favorite implements Serializable {
    private static final long serialVersionUID = 1L;

    private String favoriteId;
    private String userId;
    private LocalDateTime createdAt;
    private List<FavoriteItem> items;

    public Favorite(String favoriteId, String userId) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    public String getFavoriteId() { return favoriteId; }
    public String getUserId() { return userId; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void addMovie(String movieId) {
        if (!isMovieFavorite(movieId)) {
            items.add(new FavoriteItem(this.favoriteId, movieId));
        }
    }

    public void removeMovie(String movieId) {
        items.removeIf(item -> item.getMovieId().equals(movieId));
    }

    public boolean isMovieFavorite(String movieId) {
        return items.stream().anyMatch(item -> item.getMovieId().equals(movieId));
    }

    public List<String> getMovies() {
        List<String> movieIds = new ArrayList<>();
        for (FavoriteItem item : items) {
            movieIds.add(item.getMovieId());
        }
        return movieIds;
    }
}
