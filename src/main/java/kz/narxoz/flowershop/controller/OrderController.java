package kz.narxoz.flowershop.controller;

import jakarta.validation.Valid;
import kz.narxoz.flowershop.dto.order.CreateOrderRequest;
import kz.narxoz.flowershop.dto.order.OrderResponse;
import kz.narxoz.flowershop.security.SecurityUser;
import kz.narxoz.flowershop.service.OrderService;
import kz.narxoz.flowershop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal,
                                               @Valid @RequestBody CreateOrderRequest req) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(orderService.createOrder(user.getId(), req));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> list(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(orderService.listForUser(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOne(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal,
                                               @PathVariable Long id) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(orderService.getOne(user.getId(), id));
    }

    @PostMapping("/{id}/mark-paid")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<OrderResponse> markPaid(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.markPaid(id));
    }
}
