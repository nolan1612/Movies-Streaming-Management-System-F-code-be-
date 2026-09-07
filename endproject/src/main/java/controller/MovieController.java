package controller;

import enums.OrderType;
import enums.SortBy;
import model.Movie;
import service.MovieService;
import java.util.List;

public class MovieController {
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public List<Movie> getAllMovies() { return movieService.getAllMovies(); }
    public void addMovie(Movie movie, List<String> catNames) { movieService.addMovie(movie, catNames); }
    public boolean updateMovie(String oldTitle, String newTitle, String desc, int duration, int year, String dir, List<String> actors) {
        return movieService.updateMovie(oldTitle, newTitle, desc, duration, year, dir, actors);
    }
    public String deleteMovieByTitle(String title) { return movieService.deleteMovieByTitle(title); }
    public List<Movie> search(String keyword) { return movieService.searchMovies(keyword); }
    public List<Movie> filterByCategory(String catName) { return movieService.getMoviesByCategoryName(catName); }
    public List<Movie> sort(SortBy sortBy, OrderType orderType) { return movieService.sortMovies(sortBy, orderType); }
    public Movie getMovieByTitle(String title) { return movieService.getMovieByTitle(title); }
}