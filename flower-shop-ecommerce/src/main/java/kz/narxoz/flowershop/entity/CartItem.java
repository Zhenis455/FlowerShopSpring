package kz.narxoz.flowershop.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "cart_items",
       uniqueConstraints = @UniqueConstraint(name="uk_cart_product", columnNames = {"cart_id","product_id"}))
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private Instant addedAt = Instant.now();

    public CartItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Instant getAddedAt() { return addedAt; }
    public void setAddedAt(Instant addedAt) { this.addedAt = addedAt; }
}
