package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.order.CreateOrderRequest;
import kz.narxoz.flowershop.entity.*;
import kz.narxoz.flowershop.exception.BadRequestException;
import kz.narxoz.flowershop.repository.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Test
    void createOrder_whenCartEmpty_throws() {
        CartRepository cartRepo = mock(CartRepository.class);
        CartItemRepository cartItemRepo = mock(CartItemRepository.class);
        AddressRepository addressRepo = mock(AddressRepository.class);
        OrderRepository orderRepo = mock(OrderRepository.class);
        OrderItemRepository orderItemRepo = mock(OrderItemRepository.class);
        InventoryRepository inventoryRepo = mock(InventoryRepository.class);
        PaymentRepository paymentRepo = mock(PaymentRepository.class);
        DeliveryRepository deliveryRepo = mock(DeliveryRepository.class);

        OrderService service = new OrderService(cartRepo, cartItemRepo, addressRepo, orderRepo, orderItemRepo, inventoryRepo, paymentRepo, deliveryRepo);

        Cart cart = new Cart(); cart.setId(1L);
        when(cartRepo.findByUserId(7L)).thenReturn(Optional.of(cart));
        when(cartItemRepo.findByCartId(1L)).thenReturn(List.of());

        CreateOrderRequest req = new CreateOrderRequest();
        req.setAddressId(1L);
        req.setPaymentMethod("CARD");

        assertThrows(BadRequestException.class, () -> service.createOrder(7L, req));
    }
}
