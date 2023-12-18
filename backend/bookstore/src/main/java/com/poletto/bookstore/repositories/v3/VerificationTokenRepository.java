package com.poletto.bookstore.repositories.v3;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.User;
import com.poletto.bookstore.entities.VerificationToken;


@Repository("VerificationTokenRepositoryV3")
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	
	Optional<VerificationToken> findById(Long id);
	
	Optional<VerificationToken> findByUser(User user);
	
	Optional<VerificationToken> findByToken(UUID token);
	
	@Query("SELECT vt "
		 + "FROM VerificationToken vt "
		 + "WHERE vt.user.id = :userId "
		 + "AND vt.expiresAt > CURRENT_TIMESTAMP "
		 + "ORDER BY vt.createdAt DESC "
		 + "LIMIT 1")
	Optional<VerificationToken> findLastValidTokenByUserId(Long userId);

}
