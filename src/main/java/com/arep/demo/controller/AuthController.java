package com.arep.demo.controller;

import com.arep.demo.service.UserService;
import com.arep.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, @Value("${app.jwt.secret}") String secret) {
        this.userService = userService;
        this.jwtUtil = new JwtUtil(secret);
    }

    public static record RegisterRequest(String username, String password) {}
    public static record LoginRequest(String username, String password) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody(required = false) RegisterRequest req) {
        System.out.println("DEBUG: /api/auth/register called with -> " + req);
        if (req == null || isBlank(req.username()) || isBlank(req.password())) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password are required"));
        }

        try {
            userService.register(req.username().trim(), req.password());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("status", "registered"));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage(), "exception", e.getClass().getName()));
        } catch (Exception e) {
            // DEV: devolver el detalle de la excepción en la respuesta para depuración
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "internal server error",
                            "exception", e.getClass().getName(),
                            "message", e.getMessage()
                    ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) LoginRequest req) {
        System.out.println("DEBUG: /api/auth/login called with -> " + req);
        if (req == null || isBlank(req.username()) || isBlank(req.password())) {
            return ResponseEntity.badRequest().body(Map.of("error", "username and password are required"));
        }

        try {
            boolean ok = userService.checkPassword(req.username().trim(), req.password());
            if (!ok) return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
            String token = jwtUtil.generateToken(req.username().trim());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "internal server error", "exception", e.getClass().getName(), "message", e.getMessage()));
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
