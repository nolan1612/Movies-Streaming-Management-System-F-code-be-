/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.WatchHistory;
import utils.FileManager;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author nguyenhoangminhnhat
 */
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
        histories.addFirst(history); // Thêm vào đầu LinkedList
        save();
    }

    public List<WatchHistory> findByUserId(String userId) {
        return histories.stream()
                .filter(h -> h.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
