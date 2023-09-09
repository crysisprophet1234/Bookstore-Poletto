package com.poletto.bookstore.repositories.v2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Category;

@Repository("CategoryRepositoryV2")
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
