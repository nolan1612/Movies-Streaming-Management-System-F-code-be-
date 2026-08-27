/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import model.Category;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class CategoryRepository {
    private final String FILE_PATH = "data/categories.dat";
    private List<Category> categories;

    public CategoryRepository() {
        categories = FileManager.loadObject(FILE_PATH, List.class);
        if (categories == null) categories = new ArrayList<>();
    }

    public void save() {
        FileManager.saveObject(FILE_PATH, categories);
    }

    public List<Category> findAll() { return categories; }

    public Category findById(String categoryId) {
        return categories.stream().filter(c -> c.getCategoryId().equalsIgnoreCase(categoryId)).findFirst().orElse(null);
    }

    public void add(Category category) {
        categories.add(category);
        save();
    }
}
