package kz.narxoz.flowershop.dto.order;

import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {
    @NotNull
    private Long addressId;

    @NotNull
    private String paymentMethod; // CARD, CASH

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
