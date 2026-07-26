package com.example.filmsAPI.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.filmsAPI.common.ResourceNotFoundException;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public CategoryInfo getCategoryInfoBy(int id) {
        return categoryRepository.getCategoryBy(id).orElseThrow(() -> new ResourceNotFoundException("Categoty", id));
    }

    public List<CategoryInfo> getAllCategoryInfo() {
        return categoryRepository.getAllCategories();
    }

    public CategoryInfo addCategory(CategoryInfo categoryInfo) {
        return categoryRepository.addCategory(categoryInfo);
    }

    public void deleteCategoryBy(int id) {
        categoryRepository.deleteCategoryBy(id);
    }
}
