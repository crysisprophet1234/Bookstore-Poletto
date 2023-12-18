package com.poletto.bookstore.repositories.v3;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.User;

import io.lettuce.core.dynamic.annotation.Param;

@Repository("UserRepositoryV3")
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query("SELECT user "
		 + "FROM User user "
		 + "INNER JOIN user.roles roles "
		 + "WHERE (:roleId IS NULL OR roles.id IN :roleId) "
		 + "AND (LOWER(:userStatus) = 'all' OR LOWER(cast(user.userStatus as text)) = LOWER(:userStatus)) "
		 + "AND (LOWER(:accountStatus) = 'all' OR LOWER(cast(user.accountStatus as text)) = LOWER(:accountStatus))")
	Page<User> findAll(
		@Param("userStatus") String userStatus,
		@Param("accountStatus") String accountStatus,
		@Param("roleId") Long roleId,
		Pageable pageable
	);
	
	Optional<User> findByEmail(String email);

}