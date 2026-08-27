/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Category;
import repository.CategoryRepository;
import java.util.List;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() { return categoryRepository.findAll(); }
    public void addCategory(Category category) { categoryRepository.add(category); }
}
