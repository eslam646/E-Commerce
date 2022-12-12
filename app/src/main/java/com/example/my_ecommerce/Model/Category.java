package com.example.my_ecommerce.Model;

public class Category {
    private String Category_Name;

    public Category() {
    }

    public Category(String category_Name) {
        Category_Name = category_Name;
    }

    public String getCategory_Name() {
        return Category_Name;
    }

    public void setCategory_Name(String category_Name) {
        Category_Name = category_Name;
    }
}
