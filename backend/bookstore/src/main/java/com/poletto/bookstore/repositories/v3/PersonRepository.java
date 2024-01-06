package com.poletto.bookstore.repositories.v3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Person;

@Repository("PersonRepositoryV3")
public interface PersonRepository extends JpaRepository<Person, Long> {

}
