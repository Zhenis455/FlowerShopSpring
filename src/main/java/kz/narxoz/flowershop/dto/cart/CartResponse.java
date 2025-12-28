package kz.narxoz.flowershop.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long cartId,
        Long userId,
        List<CartItemResponse> items,
        BigDecimal total
) {}
