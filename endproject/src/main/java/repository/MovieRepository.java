package repository;

import model.Movie;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private final String FILE_PATH = "data/movies.dat";
    private List<Movie> movies;

    @SuppressWarnings("unchecked")
    public MovieRepository() {
        movies = FileManager.loadObject(FILE_PATH, List.class);
        if (movies == null) movies = new ArrayList<>();
    }

    public void save() {
        FileManager.saveObject(FILE_PATH, movies);
    }

    public List<Movie> findAll() { return movies; }

    public Movie findById(String movieId) {
        return movies.stream().filter(m -> m.getMovieId().equalsIgnoreCase(movieId)).findFirst().orElse(null);
    }

    public Movie findByTitle(String title) {
        return movies.stream().filter(m -> m.getTitle().equalsIgnoreCase(title.trim())).findFirst().orElse(null);
    }

    public void add(Movie movie) {
        movies.add(movie);
        save();
    }

    public void update(Movie movie) {
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getMovieId().equalsIgnoreCase(movie.getMovieId())) {
                movies.set(i, movie);
                save();
                return;
            }
        }
    }

    public boolean delete(String movieId) {
        boolean removed = movies.removeIf(m -> m.getMovieId().equalsIgnoreCase(movieId));
        if (removed) save();
        return removed;
    }
}