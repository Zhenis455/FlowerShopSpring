package kz.narxoz.flowershop.dto.auth;

public record AuthResponse(
        String token,
        Long userId,
        String email,
        String fullName,
        String role
) {}
