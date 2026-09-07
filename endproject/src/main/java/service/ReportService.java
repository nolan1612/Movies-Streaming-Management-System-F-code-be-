package service;

import model.Movie;
import repository.MovieRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
