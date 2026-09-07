package controller;

import enums.OrderType;
import enums.SortBy;
import model.Movie;
import service.MovieService;

import java.util.List;
import java.util.Map;

public class MovieController {
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public List<Movie> getAllMovies() { return movieService.getAllMovies(); }
    public Movie getMovieByTitle(String title) { return movieService.getMovieByTitle(title); }
    public void addMovie(Movie movie, List<String> categoryNames) { movieService.addMovie(movie, categoryNames); }
    public boolean updateMovie(String oldTitle, String newTitle, String desc, int duration, int year, String director, List<String> actors) {
        return movieService.updateMovie(oldTitle, newTitle, desc, duration, year, director, actors);
    }
    public String deleteMovieByTitle(String title) { return movieService.deleteMovieByTitle(title); }
    public List<Movie> searchMovies(String keyword) { return movieService.searchMovies(keyword); }
    public List<Movie> getMoviesByCategoryName(String categoryName) { return movieService.getMoviesByCategoryName(categoryName); }
    public List<Movie> sortMovies(SortBy sortBy, OrderType orderType) { return movieService.sortMovies(sortBy, orderType); }
    public Map<String, Integer> getTrendingCategories() { return movieService.getTrendingCategories(); }
    public void rateMovie(String movieId, boolean isLike) { movieService.rateMovie(movieId, isLike); }
}