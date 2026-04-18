package com.finance.dashboard.service;

import com.finance.dashboard.enums.Role;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.model.User;
import com.finance.dashboard.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUserRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setRole(role);
        return userRepository.save(user);
    }

    public User updateUserStatus(Long id, Boolean isActive) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setIsActive(isActive);
        return userRepository.save(user);
    }
}
