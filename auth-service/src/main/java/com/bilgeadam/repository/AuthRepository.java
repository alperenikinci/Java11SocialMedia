package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth,Long> {

    Optional<Auth> findOptionalByUsernameAndPassword(String username, String password);

    Boolean existsByUsernameAndPassword(String username, String password);
}
