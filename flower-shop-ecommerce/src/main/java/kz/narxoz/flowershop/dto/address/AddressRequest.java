package kz.narxoz.flowershop.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressRequest {
    @NotBlank @Size(max = 120)
    private String city;

    @NotBlank @Size(max = 180)
    private String street;

    @NotBlank @Size(max = 30)
    private String house;

    @Size(max = 30)
    private String apartment;

    @NotBlank @Size(max = 40)
    private String phone;

    private boolean isDefault;

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
