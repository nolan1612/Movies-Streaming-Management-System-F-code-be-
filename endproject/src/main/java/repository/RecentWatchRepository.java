package repository;

import model.RecentWatchStack;
import utils.FileManager;
import java.util.HashMap;
import java.util.Map;

public class RecentWatchRepository {
    private final String FILE_PATH = "data/recent_watches.dat";
    private Map<String, RecentWatchStack> userRecentWatches;

    @SuppressWarnings("unchecked")
    public RecentWatchRepository() {
        userRecentWatches = FileManager.loadObject(FILE_PATH, Map.class);
        if (userRecentWatches == null) userRecentWatches = new HashMap<>();
    }

    public void save() {
        FileManager.saveObject(FILE_PATH, userRecentWatches);
    }

    public RecentWatchStack findByUserId(String userId) {
        return userRecentWatches.getOrDefault(userId, new RecentWatchStack());
    }

    public void addOrUpdate(String userId, RecentWatchStack stack) {
        userRecentWatches.put(userId, stack);
        save();
    }
}
