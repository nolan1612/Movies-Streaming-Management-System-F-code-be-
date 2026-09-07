package repository;

import model.WatchHistory;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryRepository {
    private final String FILE_PATH = "data/history.dat";
    private List<WatchHistory> historyList;

    @SuppressWarnings("unchecked")
    public HistoryRepository() {
        historyList = FileManager.loadObject(FILE_PATH, List.class);
        if (historyList == null) historyList = new ArrayList<>();
    }

    public void save() {
        FileManager.saveObject(FILE_PATH, historyList);
    }

    public void addFirst(WatchHistory history) {
        historyList.add(0, history);
        save();
    }

    public List<WatchHistory> findByUserId(String userId) {
        return historyList.stream()
                .filter(h -> h.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
