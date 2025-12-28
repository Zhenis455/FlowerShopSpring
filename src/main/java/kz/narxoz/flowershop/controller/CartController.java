package kz.narxoz.flowershop.controller;

import jakarta.validation.Valid;
import kz.narxoz.flowershop.dto.cart.AddToCartRequest;
import kz.narxoz.flowershop.dto.cart.CartResponse;
import kz.narxoz.flowershop.security.SecurityUser;
import kz.narxoz.flowershop.service.CartService;
import kz.narxoz.flowershop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> get(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(cartService.getCart(user.getId()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> add(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal,
                                           @Valid @RequestBody AddToCartRequest req) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(cartService.addItem(user.getId(), req));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> updateQty(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal,
                                                 @PathVariable Long itemId,
                                                 @RequestParam int quantity) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(cartService.updateQuantity(user.getId(), itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> remove(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal,
                                       @PathVariable Long itemId) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        cartService.removeItem(user.getId(), itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clear(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        cartService.clearCart(user.getId());
        return ResponseEntity.noContent().build();
    }
}
