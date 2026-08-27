/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import service.WatchService;
import java.util.List;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class WatchController {
    private WatchService watchService;

    public WatchController(WatchService watchService) {
        this.watchService = watchService;
    }

    public void addToWatchlist(String userId, String movieId) { watchService.addToWatchlist(userId, movieId); }
    public void toggleFavorite(String userId, String movieId) { watchService.toggleFavorite(userId, movieId); }
    public void watchMovie(String userId, String movieId) { watchService.watchMovie(userId, movieId); }
    public List<String> getRecentMovies() { return watchService.getRecentStack().getRecentMovies(); }
}