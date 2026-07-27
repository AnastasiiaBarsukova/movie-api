package com.example.filmsAPI.category;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public CategoryInfo getCategoryInfo(@PathVariable int id) {
        return categoryService.getCategoryInfoBy(id);
    }
    
    @GetMapping()
    public List<CategoryInfo> getAllCategories() {
        return categoryService.getAllCategoryInfo();
    }

    @PostMapping()
    public CategoryInfo addCategory(@RequestParam String name) {
        CategoryInfo categoryInfo = new CategoryInfo(null, name);
        return categoryService.addCategory(categoryInfo);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable int id) {
        categoryService.deleteCategoryBy(id);
    }
}
