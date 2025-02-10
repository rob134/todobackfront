package com.example.todo.controller;

import com.example.todo.model.User;
import com.example.todo.service.UserService;
import com.example.todo.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8100")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User userRequest) {
    Optional<User> optionalUser = userService.findByUsername(userRequest.getUsername()); // 🔹 Obtendo Optional<User>

    if (optionalUser.isEmpty()) { // 🔹 Verificação correta de Optional
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuário não encontrado"));
    }

    User user = optionalUser.get(); // 🔹 Obtendo User corretamente

    if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
    }

    String token = jwtUtil.generateToken(user.getUsername());

    return ResponseEntity.ok(Map.of("token", token));
}


    @GetMapping("/login")
    public ResponseEntity<String> loginNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("Use POST para login.");
    }
}
