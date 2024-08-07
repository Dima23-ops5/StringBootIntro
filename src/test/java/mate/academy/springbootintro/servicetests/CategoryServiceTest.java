package mate.academy.springbootintro.servicetests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.springbootintro.dto.category.CategoryDto;
import mate.academy.springbootintro.dto.category.CreateCategoryRequestDto;
import mate.academy.springbootintro.mapper.CategoryMapper;
import mate.academy.springbootintro.model.Category;
import mate.academy.springbootintro.repository.categoty.CategoryRepository;
import mate.academy.springbootintro.service.category.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find all categories correct")
    public void findAll_FindAllCategories_Correct() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("romantic");
        category1.setDescription("romantic books");
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("fantasy");
        category2.setDescription("fantastic books");

        CategoryDto categoryDto1 = createCategoryDto(category1);
        CategoryDto categoryDto2 = createCategoryDto(category2);

        Mockito.when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        Mockito.when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);
        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<CategoryDto> result = categoryService.findAll();

        assertEquals(2, result.size());
        assertNotNull(result);
        assertEquals(categoryDto1, result.get(0));
        Mockito.verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Verify getting category by id correct")
    public void getById_GetCategoryById_Correct() {
        Category category = createCategoryForTests();
        CategoryDto categoryDto = createCategoryDto(category);

        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDto actual = categoryService.getById(1L);

        assertNotNull(actual);
        assertEquals(actual, categoryDto);
    }

    @Test
    @DisplayName("Verify getting category with not existing id")
    public void getById_GetCategoryWithNotExistedId_False() {
        Long categoryId = 10L;

        Mockito.when(categoryRepository.findById(categoryId))
                .thenThrow(new NoSuchElementException(
                        "Cannot find category with id:" + categoryId));

        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryService.getById(categoryId));
        String expected = "Cannot find category with id:" + categoryId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify saving category correct")
    public void saveCategory_Correct() {
        Category category = createCategoryForTests();
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "romantic", "romantic books");
        CategoryDto excepted = new CategoryDto(1L, requestDto.name(), requestDto.description());

        Mockito.when(categoryMapper.toDto(category)).thenReturn(excepted);
        Mockito.when(categoryMapper.toModel(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto actual = categoryService.save(requestDto);
        assertNotNull(actual);
        assertEquals(actual, excepted);
        Mockito.verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Verify updating category with correct id")
    public void updateCategory_UpdateCategoryById_Correct() {
        Category category = createCategoryForTests();
        CategoryDto categoryDto = createCategoryDto(category);
        CreateCategoryRequestDto categoryForUpdating = new CreateCategoryRequestDto(
                "fantasy", "Fantastic books");

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Mockito.doNothing().when(categoryMapper).updateCategory(categoryForUpdating, category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto excepted = categoryService.update(1L, categoryForUpdating);

        assertNotNull(excepted);
        assertEquals(excepted, categoryForUpdating);
    }

    @Test
    @DisplayName("Verify updating category with not existing id")
    public void updateCategory_UpdateCategoryWithNotExistedId_False() {
        Category category = createCategoryForTests();
        Long notValidId = 10L;
        CreateCategoryRequestDto categoryForUpdating = new CreateCategoryRequestDto(
                "fantasy", "Fantastic books");

        Mockito.when(categoryRepository.findById(notValidId)).thenThrow(
                new NoSuchElementException("Cannot found category with id: " + notValidId));

        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryService.update(notValidId, categoryForUpdating));
        String actual = exception.getMessage();
        String excepted = "Cannot found category with id: " + notValidId;

        assertEquals(actual, excepted);
    }

    private CategoryDto createCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto(category.getId(),
                category.getName(), category.getDescription());
        return categoryDto;
    }

    private Category createCategoryForTests() {
        Category category = new Category();
        category.setId(1L);
        category.setName("romantic");
        category.setDescription("romantic books");
        return category;
    }
}
