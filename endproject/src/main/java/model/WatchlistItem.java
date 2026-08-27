/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class WatchlistItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String watchlistId;
    private String movieId;
    private LocalDateTime addedAt;

    public WatchlistItem(String watchlistId, String movieId) {
        this.watchlistId = watchlistId;
        this.movieId = movieId;
        this.addedAt = LocalDateTime.now();
    }

    public String getWatchlistId() { return watchlistId; }
    public String getMovieId() { return movieId; }
    public LocalDateTime getAddedAt() { return addedAt; }
}