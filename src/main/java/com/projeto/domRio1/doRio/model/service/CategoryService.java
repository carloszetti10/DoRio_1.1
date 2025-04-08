package com.projeto.domRio1.doRio.model.service;


import com.projeto.domRio1.doRio.model.PosException;
import com.projeto.domRio1.doRio.model.entity.Category;
import com.projeto.domRio1.doRio.model.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo repo;

    public List<Category> findAll() {
        return repo.findAll();
    }

    public void save(Category category) {

        if(StringUtils.isEmpty(category.getName())) {
            throw new PosException("Please enter category name.");
        }

        repo.save(category);
    }
}
