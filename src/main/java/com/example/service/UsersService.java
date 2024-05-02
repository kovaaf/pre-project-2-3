package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UserRepository userRepository;

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> index() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User show(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public Optional<User> findByName(String name) {
        return userRepository.findUserByName(name).stream().findAny();
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email).stream().findAny();
    }

    public void update(Long id, User user) {
        user.setId(id);
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
