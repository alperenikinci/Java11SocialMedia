package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.utility.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth,Long> {

    Optional<Auth> findOptionalByUsernameAndPassword(String username, String password);
    Boolean existsByUsernameAndPassword(String username, String password);
    List<Auth> findAllByRole(ERole eRole);
}
