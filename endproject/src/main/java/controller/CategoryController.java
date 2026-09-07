package controller;

import model.Category;
import service.CategoryService;
import java.util.List;

public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<Category> getCategories() { return categoryService.getAllCategories(); }
    public boolean addCategory(Category category) { return categoryService.addCategory(category); }
    public boolean updateCategory(String oldName, String newName, String desc) { return categoryService.updateCategory(oldName, newName, desc); }
    public String deleteCategory(String name) { return categoryService.deleteCategory(name); }
}