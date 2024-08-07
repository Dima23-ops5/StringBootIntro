package mate.academy.springbootintro.controllertests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.springbootintro.dto.bookdto.BookDtoWithoutCategoryIds;
import mate.academy.springbootintro.dto.category.CategoryDto;
import mate.academy.springbootintro.dto.category.CreateCategoryRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    @Autowired
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/add-three-books.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/book/remove-all=books.sql"));
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create new category")
    @Sql(scripts = "classpath:/database/category/delete-category-science-fiction.aql")
    public void createCategory_Correct() throws Exception {
        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("Science Fiction",
                        " Typically deals with imaginative and futuristic concepts such "
                                + "as advanced science and technology");
        CategoryDto expected = new CategoryDto(4L, requestDto.name(), requestDto.description());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                post("/categories")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.id());
        assertEquals(actual.name(), expected.name());
        assertEquals(actual.name(), expected.name());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Find all categories")
    public void findAll_FindAllCategories_Correct() throws Exception {
        CategoryDto category1 = new CategoryDto(1L, "Fantasy",
                "Fantasy is a genre of speculative "
                        + "fiction involving magical elements...");
        CategoryDto category2 = new CategoryDto(2L, "Horror",
                "Novels are characterized by the fact that the main "
                        + "plot revolves around something scary and terrifying.");
        CategoryDto category3 = new CategoryDto(3L, "Biography",
                "Biographies tell the true story of a person's life.");

        List<CategoryDto> expected = List.of(category1, category2, category3);

        MvcResult result = mockMvc.perform(
                get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = Arrays.stream(objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        CategoryDto[].class)).toList();

        assertNotNull(actual);
        assertEquals(3, actual.size());
        assertEquals(actual, expected);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category by id")
    public void getById_GetCategoryByExistedId_Correct() throws Exception {
        CategoryDto expected = new CategoryDto(1L, "Fantasy",
                "Fantasy is a genre of speculative "
                        + "fiction involving magical elements...");

        MvcResult result = mockMvc.perform(
                get("/categories/1").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertEquals(actual.id(), expected.id());
        assertEquals(actual.name(), expected.name());
        assertEquals(actual.description(), expected.description());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category by id")
    public void updateCategory_UpdateCategoryWithExistedId_Correct() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto("Science Fiction",
                " Typically deals with imaginative and futuristic concepts such "
                        + "as advanced science and technology");
        CategoryDto expected = new CategoryDto(1L, requestDto.name(), requestDto.description());
        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                put("/categories/1")
                        .param(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertEquals(actual.name(), expected.name());
        assertEquals(actual.description(), expected.description());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category")
    public void deleteCategory_DeleteCategoryById_ReturnNoContent() throws Exception {
        mockMvc.perform(delete("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Search books by categories id")
    @Sql(scripts = "classpath:/database/category/create-book-with-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/delete-book-with-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void searchBooks_SearchBooksByCategoryId() throws Exception {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(1L,
                "Harry Potter",
                "Joanne Rowling", "12345", BigDecimal.valueOf(50),
                "Book about boy...", "img");
        List<BookDtoWithoutCategoryIds> excepted = List.of(bookDtoWithoutCategoryIds);

        MvcResult result = mockMvc.perform(
                get("/categories/1/books").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn();
        List<BookDtoWithoutCategoryIds> actual = Arrays.stream(objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        BookDtoWithoutCategoryIds[].class)).toList();

        assertEquals(1, actual.size());
        assertEquals(actual, excepted);
    }
}
