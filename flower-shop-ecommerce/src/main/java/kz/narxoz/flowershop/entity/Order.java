package kz.narxoz.flowershop.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address deliveryAddress;

    @Column(nullable = false, length = 30)
    private String status; // NEW, PAID, SHIPPED, DELIVERED, CANCELED

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Order() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Address getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(Address deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
