package kz.narxoz.flowershop.dto.user;

import java.time.Instant;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        String role,
        boolean enabled,
        Instant createdAt
) {}
