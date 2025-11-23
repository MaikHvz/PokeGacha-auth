package com.pokegacha.main.controller;

import com.pokegacha.main.model.User;
import com.pokegacha.main.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){ this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        String res = authService.register(user);
        if ("EMAIL_EXISTS".equals(res)) return ResponseEntity.badRequest().body(Map.of("message","Email ya registrado"));
        return ResponseEntity.ok(Map.of("message","Usuario registrado"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body){
        var opt = authService.login(body.get("email"), body.get("password"));
        if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("message","Credenciales incorrectas"));
        // Envía el usuario (o un DTO sin password)
        var user = opt.get();
        user.setPassword(null); // no enviar password
        return ResponseEntity.ok(user);
    }
}
