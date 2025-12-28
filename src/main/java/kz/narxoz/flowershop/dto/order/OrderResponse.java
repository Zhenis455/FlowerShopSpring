package kz.narxoz.flowershop.dto.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        Long addressId,
        String status,
        BigDecimal totalAmount,
        Instant createdAt,
        List<OrderItemResponse> items,
        String paymentStatus,
        String deliveryStatus
) {}
