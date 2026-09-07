package repository;

import model.Favorite;
import utils.FileManager;
import java.util.HashMap;
import java.util.Map;

public class FavoriteRepository {
    private final String FILE_PATH = "data/favorites.dat";
    private Map<String, Favorite> favorites;

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
    
    public Map<String, Favorite> findAll() {
    return favorites;
}
}
