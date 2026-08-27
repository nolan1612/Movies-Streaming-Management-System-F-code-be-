/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.Watchlist;
import utils.FileManager;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class WatchlistRepository {
    private final String FILE_PATH = "data/watchlists.dat";
    private Map<String, Watchlist> watchlists; // Key: userId

    @SuppressWarnings("unchecked")
    public WatchlistRepository() {
        watchlists = FileManager.loadObject(FILE_PATH, Map.class);
        if (watchlists == null) watchlists = new HashMap<>();
    }

    public void save() {
        FileManager.saveObject(FILE_PATH, watchlists);
    }

    public Watchlist findByUserId(String userId) {
        return watchlists.get(userId);
    }

    public void addOrUpdate(Watchlist watchlist) {
        watchlists.put(watchlist.getUserId(), watchlist);
        save();
    }
}
