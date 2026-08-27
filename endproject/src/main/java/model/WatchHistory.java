/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.WatchStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class WatchHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private String historyId;
    private String userId;
    private String movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int lastPosition; // thoi gian da xem
    private WatchStatus watchStatus;
    private LocalDateTime createdAt;

    public WatchHistory(String historyId, String userId, String movieId) {
        this.historyId = historyId;
        this.userId = userId;
        this.movieId = movieId;
        this.startTime = LocalDateTime.now();
        this.lastPosition = 0;
        this.watchStatus = WatchStatus.WATCHING;
        this.createdAt = LocalDateTime.now();
    }

    public String getHistoryId() { return historyId; }
    public String getUserId() { return userId; }
    public String getMovieId() { return movieId; }
    public int getLastPosition() { return lastPosition; }
    public WatchStatus getWatchStatus() { return watchStatus; }

    public void updateProgress(int position) {
        this.lastPosition = position;
        this.endTime = LocalDateTime.now();
    }

    public void markAsCompleted() {
        this.watchStatus = WatchStatus.COMPLETED;
        this.endTime = LocalDateTime.now();
    }

    public double getProgressPercentage(int totalDuration) {
        if (totalDuration <= 0) return 0.0;
        return Math.min(100.0, ((double) lastPosition / totalDuration) * 100);
    }
}