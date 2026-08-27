/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.Favorite;
import utils.FileManager;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class FavoriteRepository {
    private final String FILE_PATH = "data/favorites.dat";
    private Map<String, Favorite> favorites; // Key: userId

    @SuppressWarnings("unchecked")
    public FavoriteRepository() {
        favorites = FileManager.loadObject(FILE_PATH, Map.class);
        if (favorites == null) favorites = new HashMap<>();
    }

    public void save() {
        FileManager.saveObject(FILE_PATH, favorites);
    }

    public Favorite findByUserId(String userId) {
        return favorites.get(userId);
    }

    public void addOrUpdate(Favorite favorite) {
        favorites.put(favorite.getUserId(), favorite);
        save();
    }
}
