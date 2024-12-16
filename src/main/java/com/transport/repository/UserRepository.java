package com.transport.repository;
import java.util.List;

import com.transport.model.User;
import com.transport.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.transport.model.TransporterStatus;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findAll(); // Par défaut, toutes les données sont accessibles ici
    List<User> findByRoleAndStatus(UserRole role, TransporterStatus status);


}