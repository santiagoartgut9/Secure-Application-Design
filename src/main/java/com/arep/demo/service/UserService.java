package com.arep.demo.service;

import com.arep.demo.model.User;
import com.arep.demo.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    

        public User register(String username, String rawPassword) {
        System.out.println("DEBUG UserService.register -> username=" + username + " rawPasswordPresent=" + (rawPassword != null));
        if (repo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("username already exists");
        }
        String hash = encoder.encode(rawPassword); // si rawPassword es null aquí saltará NPE
        User u = new User(username, hash);
        return repo.save(u);
    }


    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }
    

    public boolean checkPassword(String username, String rawPassword) {
        return repo.findByUsername(username)
                .map(u -> encoder.matches(rawPassword, u.getPasswordHash()))
                .orElse(false);
    }
}
