package com.galvanize.jwtclient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Service
public class CategoryService {

    CategoriesRepository categoriesRepository;

    public CategoryService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }


    public List<Cat> getCategorys() {
        return categoriesRepository.findAll();
    }


    public Cat addCategory(Cat cat) {
        return categoriesRepository.save(cat);
    }

    public void deleteCategory(String name) {
        List<Cat> categories = categoriesRepository.findByName(name);
        if(categories == null || categories.isEmpty()) {
            throw new CatNotFoundException("There is not a category with the name: " + name);
        }
        for(Cat category : categories){
            categoriesRepository.delete(category);
        }
    }

    public Cat updateCategory(String name, Cat updatedCategory) {
        List<Cat> existingCategories = categoriesRepository.findByName(name);
        if(existingCategories.isEmpty()){
            return null;
        }
        Cat existingCategory = existingCategories.get(0);
        existingCategory.setName(updatedCategory.getName());
        existingCategory.setId(updatedCategory.getId());
        return categoriesRepository.save(existingCategory);
    }

}
