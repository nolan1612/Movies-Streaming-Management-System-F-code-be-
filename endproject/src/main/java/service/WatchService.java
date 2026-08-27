/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.*;
import repository.*;
import utils.IdGenerator;

import java.util.List;
/**
 *
 * @author nguyenhoangminhnhat
 */
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

    // Watchlist logic
    public void addToWatchlist(String userId, String movieId) {
        Watchlist wl = watchlistRepo.findByUserId(userId);
        if (wl == null) {
            wl = new Watchlist(IdGenerator.generateId("WL"), userId);
        }
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

    // Favorite logic
    public void toggleFavorite(String userId, String movieId) {
        Favorite fav = favoriteRepo.findByUserId(userId);
        if (fav == null) {
            fav = new Favorite(IdGenerator.generateId("FAV"), userId);
        }
        Movie movie = movieRepo.findById(movieId);
        if (fav.isMovieFavorite(movieId)) {
            fav.removeMovie(movieId);
            if (movie != null) movie.decreaseFavoriteCount();
        } else {
            fav.addMovie(movieId);
            if (movie != null) movie.increaseFavoriteCount();
        }
        favoriteRepo.addOrUpdate(fav);
        if (movie != null) movieRepo.update(movie);
    }

    // History & Streaming Logic
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
