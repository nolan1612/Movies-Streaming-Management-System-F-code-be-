package service;

import model.*;
import repository.*;
import utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class WatchService {
    private WatchlistRepository watchlistRepo;
    private FavoriteRepository favoriteRepo;
    private HistoryRepository historyRepo;
    private MovieRepository movieRepo;
    private RecentWatchStack recentStack;

    public WatchService(WatchlistRepository watchlistRepo, FavoriteRepository favoriteRepo,
                        HistoryRepository historyRepo, MovieRepository movieRepo) {
        this.watchlistRepo = watchlistRepo;
        this.favoriteRepo = favoriteRepo;
        this.historyRepo = historyRepo;
        this.movieRepo = movieRepo;
        this.recentStack = new RecentWatchStack();
    }

    // Watchlist Management
    public Watchlist getWatchlist(String userId) {
        Watchlist wl = watchlistRepo.findByUserId(userId);
        if (wl == null) {
            wl = new Watchlist(IdGenerator.generateId("WL"), userId);
            watchlistRepo.addOrUpdate(wl);
        }
        return wl;
    }

    public void addToWatchlist(String userId, String movieId) {
        Watchlist wl = getWatchlist(userId);
        wl.addMovie(movieId);
        watchlistRepo.addOrUpdate(wl);
    }

    public void removeFromWatchlist(String userId, String movieId) {
        Watchlist wl = watchlistRepo.findByUserId(userId);
        if (wl != null) {
            wl.removeMovie(movieId);
            watchlistRepo.addOrUpdate(wl);
        }
    }

    // Favorite Movie Management
    public Favorite getFavorite(String userId) {
        Favorite fav = favoriteRepo.findByUserId(userId);
        if (fav == null) {
            fav = new Favorite(IdGenerator.generateId("FAV"), userId);
            favoriteRepo.addOrUpdate(fav);
        }
        return fav;
    }

    public void addFavorite(String userId, String movieId) {
        Favorite fav = getFavorite(userId);
        if (!fav.isMovieFavorite(movieId)) {
            fav.addMovie(movieId);
            favoriteRepo.addOrUpdate(fav);
            Movie m = movieRepo.findById(movieId);
            if (m != null) {
                m.increaseFavoriteCount();
                movieRepo.update(m);
            }
        }
    }

    public void removeFavorite(String userId, String movieId) {
        Favorite fav = favoriteRepo.findByUserId(userId);
        if (fav != null && fav.isMovieFavorite(movieId)) {
            fav.removeMovie(movieId);
            favoriteRepo.addOrUpdate(fav);
            Movie m = movieRepo.findById(movieId);
            if (m != null) {
                m.decreaseFavoriteCount();
                movieRepo.update(m);
            }
        }
    }

    public List<Movie> getFavoriteMovies(String userId) {
        Favorite fav = getFavorite(userId);
        List<Movie> movies = new ArrayList<>();
        for (String mId : fav.getMovies()) {
            Movie m = movieRepo.findById(mId);
            if (m != null) movies.add(m);
        }
        return movies;
    }

    public List<Movie> getWatchlistMovies(String userId) {
        Watchlist wl = getWatchlist(userId);
        List<Movie> movies = new ArrayList<>();
        for (String mId : wl.getMovies()) {
            Movie m = movieRepo.findById(mId);
            if (m != null) movies.add(m);
        }
        return movies;
    }

    // Streaming & History
    public void watchMovie(String userId, String movieId) {
        Movie movie = movieRepo.findById(movieId);
        if (movie != null) {
            movie.increaseView();
            movieRepo.update(movie);
            
            WatchHistory history = new WatchHistory(IdGenerator.generateId("HIS"), userId, movieId);
            historyRepo.addFirst(history);
            recentStack.push(movieId);
        }
    }

    public RecentWatchStack getRecentStack() {
        return recentStack;
    }
}
