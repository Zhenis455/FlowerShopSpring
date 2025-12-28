package kz.narxoz.flowershop.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventories")
public class Inventory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", unique = true)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public Inventory() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
