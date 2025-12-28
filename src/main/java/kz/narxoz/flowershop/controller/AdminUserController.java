package kz.narxoz.flowershop.controller;

import jakarta.validation.Valid;
import kz.narxoz.flowershop.dto.admin.CreateUserRequest;
import kz.narxoz.flowershop.dto.user.UserResponse;
import kz.narxoz.flowershop.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService service;

    public AdminUserController(AdminUserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> list() {
        return ResponseEntity.ok(service.listUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest req) {
        return ResponseEntity.ok(service.createUser(req));
    }

    @PostMapping("/{id}/block")
    public ResponseEntity<UserResponse> block(@PathVariable Long id) {
        return ResponseEntity.ok(service.setBlocked(id, true));
    }

    @PostMapping("/{id}/unblock")
    public ResponseEntity<UserResponse> unblock(@PathVariable Long id) {
        return ResponseEntity.ok(service.setBlocked(id, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
