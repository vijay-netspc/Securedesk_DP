package com.securedesk.service;

import com.securedesk.dto.UserRequestDto;
import com.securedesk.entity.User;
import com.securedesk.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(UserRequestDto dto) {
        User user = new User(dto.getName(), dto.getEmail(), dto.getPassword(), dto.getRole());
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
    }
}
