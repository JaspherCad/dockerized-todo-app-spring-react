package com.jaspher.rest.webservices.restfulwebservices.authapi.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jaspher.rest.webservices.restfulwebservices.authapi.dto.LoginUserDto;
import com.jaspher.rest.webservices.restfulwebservices.authapi.dto.RegisterUserDto;
import com.jaspher.rest.webservices.restfulwebservices.customexceptions.AccountAlreadyExistsException;
import com.jaspher.rest.webservices.restfulwebservices.todo.users.repository.UserRepository;
import com.jaspher.rest.webservices.restfulwebservices.users.User;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
    	
    	if (userRepository.existsByEmail(input.getEmail())) {
            throw new AccountAlreadyExistsException("Account with email " + input.getEmail() + " already exists");
    	}
    	
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
                //.setFullName(input.getFullName())
                //.setEmail(input.getEmail())
                //.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }


    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
