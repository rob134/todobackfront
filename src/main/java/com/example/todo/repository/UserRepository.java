package com.example.todo.repository;

import com.example.todo.model.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "tasks")
    Optional<User> findByUsername(String username); // 🔹 Retorna Optional<User>
}