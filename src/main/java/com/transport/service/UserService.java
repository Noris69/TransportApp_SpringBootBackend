package com.transport.service;

import com.transport.model.User;
import com.transport.model.UserRole;
import com.transport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.transport.model.TransporterStatus;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }


    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public User updateUser(User existingUser, User updatedData) {
        existingUser.setFirstName(updatedData.getFirstName());
        existingUser.setLastName(updatedData.getLastName());
        existingUser.setPhoneNumber(updatedData.getPhoneNumber());
        if (updatedData.getPassword() != null && !updatedData.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedData.getPassword()));
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }
    public List<User> findSellersByStatus(TransporterStatus status) {
        return userRepository.findByRoleAndStatus(UserRole.SELLER, status);
    }
}
