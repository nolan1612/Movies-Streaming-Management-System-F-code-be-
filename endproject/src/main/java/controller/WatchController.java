package controller;

import model.Movie;
import model.WatchHistory;
import service.WatchService;

import java.util.List;

public class WatchController {
    private WatchService watchService;

    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }

    public String addToWatchlist(String userId, String movieId) { return watchService.addToWatchlist(userId, movieId); }
    public String removeFromWatchlist(String userId, String movieId) { return watchService.removeFromWatchlist(userId, movieId); }
    public String addFavorite(String userId, String movieId) { return watchService.addFavorite(userId, movieId); }
    public String removeFavorite(String userId, String movieId) { return watchService.removeFavorite(userId, movieId); }
    public List<Movie> getFavoriteMovies(String userId) { return watchService.getFavoriteMovies(userId); }
    public List<Movie> getWatchlistMovies(String userId) { return watchService.getWatchlistMovies(userId); }
    public void startWatching(String userId, String movieId) { watchService.startWatchingMovie(userId, movieId); }
    public WatchHistory getMovieWatchHistory(String userId, String movieId) { return watchService.getMovieWatchHistory(userId, movieId); }
    public void updateWatchProgress(String userId, String movieId, int watchedMinutes) { watchService.updateWatchProgress(userId, movieId, watchedMinutes); }
    public List<Movie> getFullWatchHistory(String userId) { return watchService.getFullWatchHistory(userId); }
    public List<String> getRecentMovies(String userId) { return watchService.getRecentMovies(userId); }
}