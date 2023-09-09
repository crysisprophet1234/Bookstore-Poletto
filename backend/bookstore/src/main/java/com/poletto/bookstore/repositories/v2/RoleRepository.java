package com.poletto.bookstore.repositories.v2;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Role;

@Repository("RoleRepositoryV2")
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByAuthority(String authority);

}
