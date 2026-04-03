package com.sanket.financedashboardapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import com.sanket.financedashboardapi.enums.Role;
import com.sanket.financedashboardapi.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    long countByRole(Role role);
}
