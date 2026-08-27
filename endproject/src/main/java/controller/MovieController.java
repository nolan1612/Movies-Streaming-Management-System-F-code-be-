/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import enums.OrderType;
import enums.SortBy;
import model.Movie;
import service.MovieService;

import java.util.List;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class MovieController {
   private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public List<Movie> getAllMovies() { return movieService.getAllMovies(); }
    public void addMovie(Movie movie) { movieService.addMovie(movie); }
    public boolean deleteMovie(String id) { return movieService.deleteMovie(id); }
    public List<Movie> search(String keyword) { return movieService.searchByTitle(keyword); }
    public List<Movie> sort(SortBy sortBy, OrderType orderType) { return movieService.sortMovies(sortBy, orderType); }
}