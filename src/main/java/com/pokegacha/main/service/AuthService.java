package com.pokegacha.main.service;

import com.pokegacha.main.model.User;
import com.pokegacha.main.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository repo) { 
        this.repo = repo; 
    }

    public String register(User user) {
        if (repo.existsByEmail(user.getEmail())) {
            return "EMAIL_EXISTS";
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCoins(1000);
        user.setTotalCartas(0);
        user.setTotalTiradas(0);
        user.setCardsList("");
        repo.save(user);
        return "OK";
    }

    public Optional<User> login(String email, String rawPassword) {
        Optional<User> u = repo.findByEmail(email);
        if (u.isEmpty()) return Optional.empty();
        if (encoder.matches(rawPassword, u.get().getPassword())) return u;
        return Optional.empty();
    }
}
