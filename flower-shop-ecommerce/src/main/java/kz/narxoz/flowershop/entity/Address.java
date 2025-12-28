package kz.narxoz.flowershop.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(nullable = false, length = 180)
    private String street;

    @Column(nullable = false, length = 30)
    private String house;

    @Column(length = 30)
    private String apartment;

    @Column(nullable = false, length = 40)
    private String phone;

    @Column(nullable = false)
    private boolean isDefault = false;

    public Address() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouse() { return house; }
    public void setHouse(String house) { this.house = house; }

    public String getApartment() { return apartment; }
    public void setApartment(String apartment) { this.apartment = apartment; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }
}
