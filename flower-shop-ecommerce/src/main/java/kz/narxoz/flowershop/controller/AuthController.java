package kz.narxoz.flowershop.controller;

import jakarta.validation.Valid;
import kz.narxoz.flowershop.dto.auth.AuthRequest;
import kz.narxoz.flowershop.dto.auth.AuthResponse;
import kz.narxoz.flowershop.dto.auth.RegisterRequest;
import kz.narxoz.flowershop.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(service.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity.ok(service.login(req));
    }
}
