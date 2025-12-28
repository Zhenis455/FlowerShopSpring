package kz.narxoz.flowershop.mapper;

import kz.narxoz.flowershop.dto.address.AddressRequest;
import kz.narxoz.flowershop.dto.address.AddressResponse;
import kz.narxoz.flowershop.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressRequest dto);

    @Mapping(target = "userId", source = "user.id")
    AddressResponse toResponse(Address entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void update(AddressRequest dto, @MappingTarget Address entity);
}
