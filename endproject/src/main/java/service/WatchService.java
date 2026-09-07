package service;

import model.*;
import repository.*;
import utils.IdGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WatchService {
    private WatchlistRepository watchlistRepo;
    private FavoriteRepository favoriteRepo;
    private HistoryRepository historyRepo;
    private MovieRepository movieRepo;
    private RecentWatchRepository recentWatchRepo;

    public WatchService(WatchlistRepository watchlistRepo, FavoriteRepository favoriteRepo,
                        HistoryRepository historyRepo, MovieRepository movieRepo, RecentWatchRepository recentWatchRepo) {
        this.watchlistRepo = watchlistRepo;
        this.favoriteRepo = favoriteRepo;
        this.historyRepo = historyRepo;
        this.movieRepo = movieRepo;
        this.recentWatchRepo = recentWatchRepo;
    }

    public Watchlist getWatchlist(String userId) {
        Watchlist wl = watchlistRepo.findByUserId(userId);
        if (wl == null) {
            wl = new Watchlist(IdGenerator.generateId("WL"), userId);
            watchlistRepo.addOrUpdate(wl);
        }
        return wl;
    }

    public String addToWatchlist(String userId, String movieId) {
        Watchlist wl = getWatchlist(userId);
        if (wl.isMovieInWatchlist(movieId)) return "Phim này đã có trong danh sách Watchlist!";
        wl.addMovie(movieId);
        watchlistRepo.addOrUpdate(wl);
        return "Thêm vào danh sách Watchlist thành công!";
    }

    public String removeFromWatchlist(String userId, String movieId) {
        Watchlist wl = watchlistRepo.findByUserId(userId);
        if (wl == null || !wl.isMovieInWatchlist(movieId)) return "LỖI: Phim này KHÔNG CÓ trong Watchlist!";
        wl.removeMovie(movieId);
        watchlistRepo.addOrUpdate(wl);
        return "Đã xóa phim khỏi danh sách Watchlist!";
    }

    public Favorite getFavorite(String userId) {
        Favorite fav = favoriteRepo.findByUserId(userId);
        if (fav == null) {
            fav = new Favorite(IdGenerator.generateId("FAV"), userId);
            favoriteRepo.addOrUpdate(fav);
        }
        return fav;
    }

    public String addFavorite(String userId, String movieId) {
        Favorite fav = getFavorite(userId);
        if (fav.isMovieFavorite(movieId)) return "Phim này đã nằm trong danh sách Yêu thích!";
        fav.addMovie(movieId);
        favoriteRepo.addOrUpdate(fav);
        
        Movie m = movieRepo.findById(movieId);
        if (m != null) { m.increaseFavoriteCount(); movieRepo.update(m); }
        return "Đã thêm phim vào danh sách Yêu thích!";
    }

    public String removeFavorite(String userId, String movieId) {
        Favorite fav = favoriteRepo.findByUserId(userId);
        if (fav == null || !fav.isMovieFavorite(movieId)) return "LỖI: Phim này KHÔNG CÓ trong danh sách Yêu thích!";
        fav.removeMovie(movieId);
        favoriteRepo.addOrUpdate(fav);
        
        Movie m = movieRepo.findById(movieId);
        if (m != null) { m.decreaseFavoriteCount(); movieRepo.update(m); }
        return "Đã xóa phim khỏi danh sách Yêu thích!";
    }

    public void removeMovieFromAllLists(String movieId) {
        for (Watchlist wl : watchlistRepo.findAll().values()) {
            if (wl.isMovieInWatchlist(movieId)) wl.removeMovie(movieId);
        }
        watchlistRepo.save();

        for (Favorite fav : favoriteRepo.findAll().values()) {
            if (fav.isMovieFavorite(movieId)) fav.removeMovie(movieId);
        }
        favoriteRepo.save();
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

    public void startWatchingMovie(String userId, String movieId) {
        Movie movie = movieRepo.findById(movieId);
        if (movie != null) {
            movie.increaseView();
            movieRepo.update(movie);
        }
    }

    public WatchHistory getMovieWatchHistory(String userId, String movieId) {
        List<WatchHistory> userHistory = historyRepo.findByUserId(userId);
        if (userHistory != null) {
            for (WatchHistory h : userHistory) {
                if (h.getMovieId().equals(movieId)) return h;
            }
        }
        return null;
    }

    public void updateWatchProgress(String userId, String movieId, int watchedMinutes) {
        List<WatchHistory> userHistory = historyRepo.findByUserId(userId);
        WatchHistory currentHistory = null;
        
        if (userHistory != null) {
            for (WatchHistory h : userHistory) {
                if (h.getMovieId().equals(movieId)) {
                    currentHistory = h;
                    break;
                }
            }
        }
        
        if (currentHistory == null) {
            currentHistory = new WatchHistory(IdGenerator.generateId("HIS"), userId, movieId);
            historyRepo.addFirst(currentHistory);
        }
        
        currentHistory.setWatchedDuration(watchedMinutes);
        currentHistory.setWatchDate(LocalDateTime.now());
        
        RecentWatchStack stack = recentWatchRepo.findByUserId(userId);
        stack.push(movieId);
        recentWatchRepo.addOrUpdate(userId, stack);
    }

    public List<Movie> getFullWatchHistory(String userId) {
        List<Movie> movies = new ArrayList<>();
        List<WatchHistory> userHistory = historyRepo.findByUserId(userId);
        if (userHistory != null) {
            for (WatchHistory h : userHistory) {
                Movie m = movieRepo.findById(h.getMovieId());
                if (m != null) movies.add(m);
            }
        }
        return movies;
    }

    public List<String> getRecentMovies(String userId) {
        RecentWatchStack stack = recentWatchRepo.findByUserId(userId);
        return stack != null ? stack.getRecentMovies() : new ArrayList<>();
    }
}
