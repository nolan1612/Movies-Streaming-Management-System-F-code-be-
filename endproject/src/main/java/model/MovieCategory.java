/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class MovieCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    private String movieId;
    private String categoryId;

    public MovieCategory(String movieId, String categoryId) {
        this.movieId = movieId;
        this.categoryId = categoryId;
    }

    public String getMovieId() { return movieId; }
    public String getCategoryId() { return categoryId; }
}
