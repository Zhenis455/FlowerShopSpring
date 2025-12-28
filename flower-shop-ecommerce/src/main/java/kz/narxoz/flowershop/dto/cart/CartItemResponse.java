package kz.narxoz.flowershop.dto.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        BigDecimal price,
        int quantity
) {}
