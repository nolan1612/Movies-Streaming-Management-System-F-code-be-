package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import model.Category;
import service.CategoryService;
import java.util.List;
/**
 *
 * @author nguyenhoangminhnhat
 */
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<Category> getCategories() { return categoryService.getAllCategories(); }
    public void addCategory(Category category) { categoryService.addCategory(category); }
}