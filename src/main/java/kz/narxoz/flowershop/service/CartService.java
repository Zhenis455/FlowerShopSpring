package kz.narxoz.flowershop.service;

import kz.narxoz.flowershop.dto.cart.AddToCartRequest;
import kz.narxoz.flowershop.dto.cart.CartItemResponse;
import kz.narxoz.flowershop.dto.cart.CartResponse;
import kz.narxoz.flowershop.entity.CartItem;
import kz.narxoz.flowershop.exception.BadRequestException;
import kz.narxoz.flowershop.exception.NotFoundException;
import kz.narxoz.flowershop.repository.CartItemRepository;
import kz.narxoz.flowershop.repository.CartRepository;
import kz.narxoz.flowershop.repository.InventoryRepository;
import kz.narxoz.flowershop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository itemRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository itemRepository,
                       ProductRepository productRepository,
                       InventoryRepository inventoryRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public CartResponse getCart(Long userId) {
        var cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        var items = itemRepository.findByCartId(cart.getId());
        List<CartItemResponse> respItems = items.stream().map(i ->
                new CartItemResponse(
                        i.getId(),
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getProduct().getPrice(),
                        i.getQuantity()
                )
        ).toList();

        BigDecimal total = items.stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), userId, respItems, total);
    }

    @Transactional
    public CartResponse addItem(Long userId, AddToCartRequest req) {
        var cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        var product = productRepository.findById(req.getProductId()).orElseThrow(() -> new NotFoundException("Product not found"));

        var inv = inventoryRepository.findByProductId(product.getId()).orElseThrow(() -> new NotFoundException("Inventory not found"));
        if (req.getQuantity() > inv.getQuantity()) {
            throw new BadRequestException("Not enough stock");
        }

        CartItem item = itemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setCart(cart);
                    ci.setProduct(product);
                    ci.setQuantity(0);
                    return ci;
                });

        int newQty = item.getQuantity() + req.getQuantity();
        if (newQty > inv.getQuantity()) throw new BadRequestException("Not enough stock");

        item.setQuantity(newQty);
        itemRepository.save(item);

        return getCart(userId);
    }

    @Transactional
    public CartResponse updateQuantity(Long userId, Long itemId, int quantity) {
        if (quantity < 1) throw new BadRequestException("Quantity must be >= 1");
        var cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        var item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Cart item not found"));
        if (!item.getCart().getId().equals(cart.getId())) throw new NotFoundException("Cart item not found");

        var inv = inventoryRepository.findByProductId(item.getProduct().getId()).orElseThrow(() -> new NotFoundException("Inventory not found"));
        if (quantity > inv.getQuantity()) throw new BadRequestException("Not enough stock");

        item.setQuantity(quantity);
        return getCart(userId);
    }

    @Transactional
    public void removeItem(Long userId, Long itemId) {
        var cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        var item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Cart item not found"));
        if (!item.getCart().getId().equals(cart.getId())) throw new NotFoundException("Cart item not found");
        itemRepository.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        var cart = cartRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        itemRepository.deleteByCartId(cart.getId());
    }
}
