package kz.narxoz.flowershop.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @Column(nullable = false, length = 30)
    private String status; // PREPARING, OUT_FOR_DELIVERY, DELIVERED

    @Column(length = 120)
    private String courierName;

    @Column(length = 40)
    private String courierPhone;

    private Instant deliveredAt;

    public Delivery() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCourierName() { return courierName; }
    public void setCourierName(String courierName) { this.courierName = courierName; }

    public String getCourierPhone() { return courierPhone; }
    public void setCourierPhone(String courierPhone) { this.courierPhone = courierPhone; }

    public Instant getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; }
}
