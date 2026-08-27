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
public class FavoriteItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String favoriteId;
    private String movieId;
    private LocalDateTime addedAt;

    public FavoriteItem(String favoriteId, String movieId) {
        this.favoriteId = favoriteId;
        this.movieId = movieId;
        this.addedAt = LocalDateTime.now();
    }

    public String getFavoriteId() { return favoriteId; }
    public String getMovieId() { return movieId; }
    public LocalDateTime getAddedAt() { return addedAt; }
}
