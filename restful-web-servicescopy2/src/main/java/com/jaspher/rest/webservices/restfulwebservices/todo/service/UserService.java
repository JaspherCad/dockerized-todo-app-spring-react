package com.jaspher.rest.webservices.restfulwebservices.todo.service;

import org.springframework.stereotype.Service;

import com.jaspher.rest.webservices.restfulwebservices.todo.users.repository.UserRepository;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}