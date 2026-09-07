package repository;

import model.Watchlist;
import utils.FileManager;
import java.util.HashMap;
import java.util.Map;

public class WatchlistRepository {
    private final String FILE_PATH = "data/watchlists.dat";
    private Map<String, Watchlist> watchlists;

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
    
    public Map<String, Watchlist> findAll() {
    return watchlists;
}
}
