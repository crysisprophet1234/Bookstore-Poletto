package com.poletto.bookstore.repositories.v1;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poletto.bookstore.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByAuthority(String authority);

}
