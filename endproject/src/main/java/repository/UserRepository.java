/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.User;
import utils.FileManager;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class UserRepository {
    private final String FILE_PATH = "data/users.dat";
    private Map<String, User> userMap; // Key: username

    @SuppressWarnings("unchecked")
    public UserRepository() {
        userMap = FileManager.loadObject(FILE_PATH, Map.class);
        if (userMap == null) userMap = new HashMap<>();
    }

    public void save() {
        FileManager.saveObject(FILE_PATH, userMap);
    }

    public Map<String, User> findAll() { return userMap; }

    public User findByUsername(String username) {
        return userMap.get(username.toLowerCase());
    }

    public void add(User user) {
        userMap.put(user.getUsername().toLowerCase(), user);
        save();
    }

    public void update(User user) {
        userMap.put(user.getUsername().toLowerCase(), user);
        save();
    }
}
