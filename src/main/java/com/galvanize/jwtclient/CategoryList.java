package com.galvanize.jwtclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CategoryList {

    private List<Cat> categories;

    public CategoryList(List<Cat> categories) {this.categories = categories;}

    public CategoryList() {this.categories = new ArrayList<>();}



    public List<Cat> getCategories() {
        return categories;
    }

    public void setCategory(List<Cat> categories) {
        this.categories = categories;
    }

    public boolean isEmpty() {
        return this.categories.isEmpty();
    }

    @Override
    public String toString(){
        return "CategoryList{" +
                "categories="+ categories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryList hubList = (CategoryList) o;
        return Objects.equals(categories, hubList.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categories);
    }

}