package repository;

import model.Category;
import utils.FileManager;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private final String FILE_PATH = "data/categories.dat";
    private List<Category> categories;

    @SuppressWarnings("unchecked")
    public CategoryRepository() {
        categories = FileManager.loadObject(FILE_PATH, List.class);
        if (categories == null) categories = new ArrayList<>();
    }

    public void save() { FileManager.saveObject(FILE_PATH, categories); }

    public List<Category> findAll() { return categories; }

    public Category findById(String categoryId) {
        return categories.stream().filter(c -> c.getCategoryId().equalsIgnoreCase(categoryId)).findFirst().orElse(null);
    }

    public Category findByName(String name) {
        return categories.stream().filter(c -> c.getName().equalsIgnoreCase(name.trim())).findFirst().orElse(null);
    }

    public void add(Category category) {
        categories.add(category);
        save();
    }

    public void update(Category category) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategoryId().equals(category.getCategoryId())) {
                categories.set(i, category);
                save();
                return;
            }
        }
    }

    public boolean delete(String categoryId) {
        boolean removed = categories.removeIf(c -> c.getCategoryId().equals(categoryId));
        if (removed) save();
        return removed;
    }
}