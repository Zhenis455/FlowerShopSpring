package kz.narxoz.flowershop.dto.address;

public record AddressResponse(
        Long id,
        Long userId,
        String city,
        String street,
        String house,
        String apartment,
        String phone,
        boolean isDefault
) {}
