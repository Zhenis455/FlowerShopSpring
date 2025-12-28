package kz.narxoz.flowershop.mapper;

import kz.narxoz.flowershop.dto.product.ProductResponse;
import kz.narxoz.flowershop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "stock", expression = "java(product.getInventory() == null ? 0 : product.getInventory().getQuantity())")
    ProductResponse toResponse(Product product);
}
