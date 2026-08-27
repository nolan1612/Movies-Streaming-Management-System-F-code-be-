/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import enums.OrderType;
import enums.SortBy;
import model.Movie;
import repository.MovieRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class MovieService {
    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(String id) {
        return movieRepository.findById(id);
    }

    public void addMovie(Movie movie) {
        movieRepository.add(movie);
    }

    public boolean deleteMovie(String id) {
        return movieRepository.delete(id);
    }

    // Searching Algorithm
    public List<Movie> searchByTitle(String keyword) {
        if (keyword == null || keyword.isEmpty()) return new ArrayList<>();
        return movieRepository.findAll().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Sorting Algorithms
    public List<Movie> sortMovies(SortBy sortBy, OrderType orderType) {
        List<Movie> list = new ArrayList<>(movieRepository.findAll());
        Comparator<Movie> comparator = null;

        switch (sortBy) {
            case TITLE:
                comparator = Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER);
                break;
            case RATING:
                comparator = Comparator.comparingDouble(Movie::getRating);
                break;
            case RELEASE_YEAR:
                comparator = Comparator.comparingInt(Movie::getReleaseYear);
                break;
            case POPULARITY:
                comparator = Comparator.comparingInt(Movie::getViews);
                break;
        }

        if (comparator != null) {
            if (orderType == OrderType.DESC) {
                comparator = comparator.reversed();
            }
            list.sort(comparator);
        }
        return list;
    }
}
