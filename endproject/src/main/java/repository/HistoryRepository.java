package repository;

import model.WatchHistory;
import utils.FileManager;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryRepository {
    private final String FILE_PATH = "data/history.dat";
    private LinkedList<WatchHistory> histories;

    @SuppressWarnings("unchecked")
    public HistoryRepository() {
        histories = FileManager.loadObject(FILE_PATH, LinkedList.class);
        if (histories == null) histories = new LinkedList<>();
    }

    public void save() {
        FileManager.saveObject(FILE_PATH, histories);
    }

    public void addFirst(WatchHistory history) {
        histories.addFirst(history);
        save();
    }

    public List<WatchHistory> findByUserId(String userId) {
        return histories.stream()
                .filter(h -> h.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
