package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUserByNameIgnoreCase(String name);
    Optional<User> findByNameIgnoreCase(String name);
    Optional<User> findByName(String name);
    List<User> findUserByEmailIgnoreCase(String email);
}
