package service;

import model.Category;
import repository.CategoryRepository;
import repository.MovieCategoryRepository;
import java.util.List;

public class CategoryService {
    private CategoryRepository categoryRepository;
    private MovieCategoryRepository movieCategoryRepository;

    public CategoryService(CategoryRepository categoryRepository, MovieCategoryRepository movieCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.movieCategoryRepository = movieCategoryRepository;
    }

    public List<Category> getAllCategories() { return categoryRepository.findAll(); }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public boolean addCategory(Category category) {
        if (getCategoryByName(category.getName()) != null) return false;
        categoryRepository.add(category);
        return true;
    }

    public boolean updateCategory(String oldName, String newName, String newDesc) {
        Category cat = getCategoryByName(oldName);
        if (cat == null) return false;
        
        cat.setName(newName);
        cat.setDescription(newDesc);
        categoryRepository.update(cat);
        return true;
    }

   public String deleteCategory(String name) {
        Category cat = getCategoryByName(name);
        if (cat == null) return "LỖI: Không tìm thấy thể loại nào có tên '" + name + "'!";
        
        if (movieCategoryRepository.hasMoviesInCategory(cat.getCategoryId())) {
            return "LỖI VALIDATE: Không thể xóa thể loại '" + name + "' vì hiện đang có phim thuộc thể loại này!";
        }
        
        categoryRepository.delete(cat.getCategoryId());
        return "Thành công: Đã xóa thể loại '" + name + "'!";
    }
}