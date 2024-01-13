package com.example.backend.dao;

import com.example.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository  extends JpaRepository<User, UUID> {

    Optional<User> getByEmail(String email);
}
