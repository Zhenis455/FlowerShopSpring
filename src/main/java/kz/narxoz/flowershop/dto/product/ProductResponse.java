package kz.narxoz.flowershop.dto.product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Long categoryId,
        String categoryName,
        BigDecimal price,
        boolean active,
        String imageUrl,
        int stock
) {}
