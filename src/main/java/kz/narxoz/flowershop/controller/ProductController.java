package kz.narxoz.flowershop.controller;

import jakarta.validation.Valid;
import kz.narxoz.flowershop.dto.product.ProductRequest;
import kz.narxoz.flowershop.dto.product.ProductResponse;
import kz.narxoz.flowershop.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list(@RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(service.listActive(categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
