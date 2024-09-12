package com.galvanize.jwtclient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin("*")
public class CategoriesController {

    private CategoryService categoryService;

    public CategoriesController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/admin/categories")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CategoryList> getCategory() {
        List<Cat> categories = categoryService.getCategorys();
        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new CategoryList(categories));
    }

    @PostMapping("/api/admin/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cat> addCategory(@RequestBody Cat cat){
        try{
            cat = categoryService.addCategory(cat);
        } catch (InvalidCatException e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cat);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void invalidCatExceptionHandler(InvalidCatException e){

    }

    @ExceptionHandler(CatNotFoundException.class)
    public ResponseEntity<String> handleCatNotFoundException(CatNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @DeleteMapping("/api/admin/categories/{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasRole('Admin')")
    public void deleteCategory(@PathVariable String name){
        Cat category;
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Category name must not be empty");
        }
        try {
            categoryService.deleteCategory(name);
        } catch (CatNotFoundException x) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
    }

    @PatchMapping("/api/admin/categories/{name}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Cat> updateCategory(@PathVariable String name, @RequestBody Cat category) {
        try{
            category = categoryService.updateCategory(name, category);
        } catch (InvalidCatException y){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }


}


