package com.poletto.bookstore.repositories.v3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poletto.bookstore.entities.Address;

@Repository("AddressRepositoryV3")
public interface AddressRepository extends JpaRepository<Address, Long> {

}
