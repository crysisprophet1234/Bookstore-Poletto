package com.poletto.bookstore.repositories.v2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
