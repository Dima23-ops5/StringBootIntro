package mate.academy.springbootintro.service.category.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.category.CategoryDto;
import mate.academy.springbootintro.mapper.CategoryMapper;
import mate.academy.springbootintro.model.Category;
import mate.academy.springbootintro.repository.categoty.CategoryRepository;
import mate.academy.springbootintro.service.category.CategoryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(
                categoryRepository.findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Cannot find category with id:" + id
                                )
                        )
        );
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        return categoryMapper.toDto(
                categoryRepository.save(categoryMapper.toModel(categoryDto))
        );
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Cannot found category with id: " + id
                        )
                );
        categoryMapper.updateCategory(categoryDto, category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
