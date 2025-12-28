package kz.narxoz.flowershop.controller;

import jakarta.validation.Valid;
import kz.narxoz.flowershop.dto.address.AddressRequest;
import kz.narxoz.flowershop.dto.address.AddressResponse;
import kz.narxoz.flowershop.security.SecurityUser;
import kz.narxoz.flowershop.service.AddressService;
import kz.narxoz.flowershop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    public AddressController(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> list(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(addressService.listForUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<AddressResponse> create(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal,
                                                 @Valid @RequestBody AddressRequest req) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(addressService.create(user, req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> update(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal,
                                                 @PathVariable Long id,
                                                 @Valid @RequestBody AddressRequest req) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        return ResponseEntity.ok(addressService.update(user, id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@org.springframework.security.core.annotation.AuthenticationPrincipal SecurityUser principal,
                                       @PathVariable Long id) {
        var user = userService.getByEmailOrThrow(principal.getUsername());
        addressService.delete(user, id);
        return ResponseEntity.noContent().build();
    }
}
