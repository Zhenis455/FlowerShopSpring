package kz.narxoz.flowershop.mapper;

import kz.narxoz.flowershop.dto.user.UserResponse;
import kz.narxoz.flowershop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", expression = "java(user.getRole().getName())")
    UserResponse toResponse(User user);
}
