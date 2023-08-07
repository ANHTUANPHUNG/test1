package com.example.test.Service;

import com.example.test.DAO.CategoryDAO;
import com.example.test.Model.Category;
import com.example.test.Model.Teacher;

import java.util.List;

public class CategoryService {
    public static List<Category> getCategorys(){
        return new CategoryDAO().getCategorys();
    }
}
