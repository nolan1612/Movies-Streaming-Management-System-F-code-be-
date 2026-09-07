package service;

import enums.OrderType;
import enums.SortBy;
import model.Category;
import model.Movie;
import model.MovieCategory;
import repository.CategoryRepository;
import repository.MovieCategoryRepository;
import repository.MovieRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {
    private MovieRepository movieRepository;
    private CategoryRepository categoryRepository;
    private MovieCategoryRepository movieCategoryRepository;

    public MovieService(MovieRepository movieRepository, CategoryRepository categoryRepository, MovieCategoryRepository movieCategoryRepository) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
        this.movieCategoryRepository = movieCategoryRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(String id) {
        return movieRepository.findById(id);
    }

    public Movie getMovieByTitle(String title) {
        return movieRepository.findAll().stream()
                .filter(m -> m.getTitle().equalsIgnoreCase(title.trim()))
                .findFirst()
                .orElse(null);
    }

    public void addMovie(Movie movie, List<String> categoryIds) {
        movieRepository.add(movie);
        if (categoryIds != null) {
            for (String catId : categoryIds) {
                movieCategoryRepository.add(new MovieCategory(movie.getMovieId(), catId));
            }
        }
    }

    public void updateMovie(Movie movie) {
        movieRepository.update(movie);
    }

    public boolean deleteMovie(String id) {
        return movieRepository.delete(id);
    }

    // Searching Algorithm: title, actor, director, genre
    public List<Movie> searchMovies(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return new ArrayList<>();
        String kw = keyword.toLowerCase().trim();

        return movieRepository.findAll().stream().filter(m -> {
            boolean matchTitle = m.getTitle().toLowerCase().contains(kw);
            boolean matchDirector = m.getDirector() != null && m.getDirector().toLowerCase().contains(kw);
            boolean matchActor = m.getActors() != null && m.getActors().stream().anyMatch(a -> a.toLowerCase().contains(kw));
            
            // Check Genre (Category Name)
            List<String> catIds = movieCategoryRepository.getCategoryIdsByMovie(m.getMovieId());
            boolean matchGenre = catIds.stream().anyMatch(catId -> {
                Category cat = categoryRepository.findById(catId);
                return cat != null && cat.getName().toLowerCase().contains(kw);
            });

            return matchTitle || matchDirector || matchActor || matchGenre;
        }).collect(Collectors.toList());
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
