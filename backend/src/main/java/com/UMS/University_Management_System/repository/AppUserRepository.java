package com.UMS.University_Management_System.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UMS.University_Management_System.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
