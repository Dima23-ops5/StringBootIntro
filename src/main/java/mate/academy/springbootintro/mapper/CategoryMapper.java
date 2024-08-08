package mate.academy.springbootintro.mapper;

import mate.academy.springbootintro.config.MapperConfig;
import mate.academy.springbootintro.dto.category.CategoryDto;
import mate.academy.springbootintro.dto.category.CreateCategoryRequestDto;
import mate.academy.springbootintro.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CreateCategoryRequestDto requestDto);

    void updateCategory(CreateCategoryRequestDto requestDto, @MappingTarget Category category);
}
