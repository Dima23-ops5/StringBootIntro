package mate.academy.springbootintro.service.category;

import java.util.List;
import mate.academy.springbootintro.dto.category.CategoryDto;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
