package model;

import java.time.LocalDateTime;

public class WatchHistory {
    private String historyId;
    private String userId;
    private String movieId;
    private LocalDateTime watchDate;
    private int watchedDuration; 

    public WatchHistory(String historyId, String userId, String movieId) {
        this.historyId = historyId;
        this.userId = userId;
        this.movieId = movieId;
        this.watchDate = LocalDateTime.now();
        this.watchedDuration = 0;
    }

    public String getHistoryId() { return historyId; }
    public String getUserId() { return userId; }
    public String getMovieId() { return movieId; }
    public LocalDateTime getWatchDate() { return watchDate; }
    public void setWatchDate(LocalDateTime watchDate) { this.watchDate = watchDate; }
    public int getWatchedDuration() { return watchedDuration; }
    public void setWatchedDuration(int watchedDuration) { this.watchedDuration = watchedDuration; }
}