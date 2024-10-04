package mate.academy.springbootintro.service.category;

import java.util.List;
import mate.academy.springbootintro.dto.category.CategoryDto;
import mate.academy.springbootintro.dto.category.CreateCategoryRequestDto;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto update(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
