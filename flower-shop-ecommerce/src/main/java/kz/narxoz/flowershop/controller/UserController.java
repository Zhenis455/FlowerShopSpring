package kz.narxoz.flowershop.controller;

import jakarta.validation.Valid;
import kz.narxoz.flowershop.dto.user.ChangePasswordRequest;
import kz.narxoz.flowershop.dto.user.UpdateProfileRequest;
import kz.narxoz.flowershop.dto.user.UserResponse;
import kz.narxoz.flowershop.security.SecurityUser;
import kz.narxoz.flowershop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<UserResponse> profile(@AuthenticationPrincipal SecurityUser principal) {
        return ResponseEntity.ok(service.getProfile(principal.getUsername()));
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(@AuthenticationPrincipal SecurityUser principal,
                                              @Valid @RequestBody UpdateProfileRequest req) {
        return ResponseEntity.ok(service.updateProfile(principal.getUsername(), req));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal SecurityUser principal,
                                              @Valid @RequestBody ChangePasswordRequest req) {
        service.changePassword(principal.getUsername(), req);
        return ResponseEntity.noContent().build();
    }
}
