package repository;

import model.MovieCategory;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieCategoryRepository {
    private final String FILE_PATH = "data/movie_categories.dat";
    private List<MovieCategory> movieCategories;

    @SuppressWarnings("unchecked")
    public MovieCategoryRepository() {
        movieCategories = FileManager.loadObject(FILE_PATH, List.class);
        if (movieCategories == null) movieCategories = new ArrayList<>();
    }

    public void save() { FileManager.saveObject(FILE_PATH, movieCategories); }

    public List<MovieCategory> findAll() { return movieCategories; }

    public void add(MovieCategory movieCategory) {
        movieCategories.add(movieCategory);
        save();
    }

    public List<String> getCategoryIdsByMovie(String movieId) {
        return movieCategories.stream()
                .filter(mc -> mc.getMovieId().equalsIgnoreCase(movieId))
                .map(MovieCategory::getCategoryId)
                .collect(Collectors.toList());
    }

    public List<String> getMovieIdsByCategory(String categoryId) {
        return movieCategories.stream()
                .filter(mc -> mc.getCategoryId().equalsIgnoreCase(categoryId))
                .map(MovieCategory::getMovieId)
                .collect(Collectors.toList());
    }

    public boolean hasMoviesInCategory(String categoryId) {
        return movieCategories.stream().anyMatch(mc -> mc.getCategoryId().equalsIgnoreCase(categoryId));
    }

    public void deleteByMovieId(String movieId) {
        boolean removed = movieCategories.removeIf(mc -> mc.getMovieId().equalsIgnoreCase(movieId));
        if (removed) save();
    }

    public void deleteByCategoryId(String categoryId) {
        boolean removed = movieCategories.removeIf(mc -> mc.getCategoryId().equalsIgnoreCase(categoryId));
        if (removed) save();
    }
}