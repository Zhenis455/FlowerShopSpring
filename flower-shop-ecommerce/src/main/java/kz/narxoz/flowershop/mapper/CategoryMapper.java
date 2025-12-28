package kz.narxoz.flowershop.mapper;

import kz.narxoz.flowershop.dto.category.CategoryRequest;
import kz.narxoz.flowershop.dto.category.CategoryResponse;
import kz.narxoz.flowershop.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest dto);
    CategoryResponse toResponse(Category entity);
    void update(CategoryRequest dto, @MappingTarget Category entity);
}
