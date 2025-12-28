package kz.narxoz.flowershop.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "carts")
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public Cart() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
