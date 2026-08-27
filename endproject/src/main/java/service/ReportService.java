package service;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import model.Movie;
import repository.MovieRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class ReportService {
    private MovieRepository movieRepository;

    public ReportService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getTopViewedMovies(int limit) {
        return movieRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Movie::getViews).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Movie> getTopRatedMovies(int limit) {
        return movieRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
