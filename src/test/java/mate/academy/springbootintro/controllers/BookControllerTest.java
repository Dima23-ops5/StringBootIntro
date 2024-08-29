package mate.academy.springbootintro.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.springbootintro.dto.bookdto.BookDto;
import mate.academy.springbootintro.dto.bookdto.CreateBookRequestDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    @Autowired
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private WebApplicationContext applicationContext;

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
                    new ClassPathResource("database/book/remove-all-books.sql"));
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all books")
    public void getAll_FindAllBooks_Correct() throws Exception {
        BookDto bookHarryPotter = new BookDto();
        bookHarryPotter.setId(1L);
        bookHarryPotter.setTitle("Harry Potter");
        bookHarryPotter.setAuthor("Joanne Rowling");
        bookHarryPotter.setIsbn("12345");
        bookHarryPotter.setPrice(BigDecimal.valueOf(50));
        bookHarryPotter.setDescription("Book about boy...");
        bookHarryPotter.setCoverImage("img.harry.potter");

        BookDto bookLittlePrince = new BookDto();
        bookLittlePrince.setId(2L);
        bookLittlePrince.setTitle("The little prince");
        bookLittlePrince.setAuthor("Antoine de Saint-Exupery");
        bookLittlePrince.setIsbn("4557839");
        bookLittlePrince.setPrice(BigDecimal.valueOf(80));
        bookLittlePrince.setDescription("Story about little prince");
        bookLittlePrince.setCoverImage("img.little.prince");

        BookDto bookHobbit = new BookDto();
        bookHobbit.setId(3L);
        bookHobbit.setTitle("The hobbit");
        bookHobbit.setAuthor("J. R. R. Tolkien");
        bookHobbit.setIsbn("93587");
        bookHobbit.setPrice(BigDecimal.valueOf(40));
        bookHobbit.setDescription("Book about hobbit Bilbo");
        bookHobbit.setCoverImage("img.hobbit");

        List<BookDto> excepted = List.of(bookHarryPotter, bookLittlePrince, bookHobbit);

        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] resultBooks = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        List<BookDto> actual = Arrays.stream(resultBooks)
                .sorted(Comparator.comparingLong(BookDto::getId))
                .collect(Collectors.toList());
        assertEquals(3, actual.size());
        assertEquals(excepted, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/book/delete-book-war-and-peace.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create new book")
    public void createBook_CreateNewBook_Correct() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("War and peace");
        requestDto.setAuthor("Leo Tolstoy");
        requestDto.setIsbn("23553234");
        requestDto.setPrice(BigDecimal.valueOf(100));
        requestDto.setDescription("Book about war");
        requestDto.setCoverImage("img.war.peace");

        BookDto expected = new BookDto();
        expected.setTitle(requestDto.getTitle());
        expected.setAuthor(requestDto.getAuthor());
        expected.setIsbn(requestDto.getIsbn());
        expected.setPrice(requestDto.getPrice());
        expected.setDescription(requestDto.getDescription());
        expected.setCoverImage(requestDto.getCoverImage());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual.getId());
        assertEquals(actual.getTitle(), expected.getTitle());
        assertEquals(actual.getAuthor(), expected.getAuthor());
        assertEquals(actual.getIsbn(),expected.getIsbn());
        assertEquals(actual.getPrice(), expected.getPrice());
        assertEquals(actual.getDescription(), expected.getDescription());
        assertEquals(actual.getCoverImage(), expected.getCoverImage());
        assertEquals(actual.getCategoryIds(), expected.getCategoryIds());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get book by id")
    public void getBookById_FindBookWithValidId_Correct() throws Exception {
        BookDto excepted = new BookDto();
        excepted.setId(1L);
        excepted.setTitle("Harry Potter");
        excepted.setAuthor("Joanne Rowling");
        excepted.setIsbn("12345");
        excepted.setPrice(BigDecimal.valueOf(50));
        excepted.setDescription("Book about boy...");
        excepted.setCoverImage("img.harry.potter");

        MvcResult result = mockMvc.perform(get("/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertEquals(excepted.getId(), actual.getId());
        assertEquals(excepted.getTitle(), actual.getTitle());
        assertEquals(excepted.getAuthor(), actual.getAuthor());
        assertEquals(excepted.getIsbn(), actual.getIsbn());
        assertEquals(excepted.getPrice(), actual.getPrice());
        assertEquals(excepted.getDescription(), actual.getDescription());
        assertEquals(excepted.getCoverImage(), actual.getCoverImage());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:/database/book/add-book-for-updating.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/database/book/delete-updated-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update book")
    public void updateBookByID_UpdateBookWithExistedId_Correct() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Dune");
        requestDto.setAuthor("Frank Herbert");
        requestDto.setIsbn("354634895");
        requestDto.setPrice(BigDecimal.valueOf(150));
        requestDto.setDescription("Novel about future");
        requestDto.setCoverImage("img.dune");

        BookDto excepted = new BookDto();
        excepted.setId(4L);
        excepted.setTitle(requestDto.getTitle());
        excepted.setAuthor(requestDto.getAuthor());
        excepted.setIsbn(requestDto.getIsbn());
        excepted.setPrice(requestDto.getPrice());
        excepted.setDescription(requestDto.getDescription());
        excepted.setCoverImage(requestDto.getCoverImage());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/4")
                .content(requestJson)
                 .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertEquals(excepted.getId(), actual.getId());
        assertEquals(excepted.getTitle(), actual.getTitle());
        assertEquals(excepted.getAuthor(), actual.getAuthor());
        assertEquals(excepted.getIsbn(), actual.getIsbn());
        assertEquals(excepted.getPrice(), actual.getPrice());
        assertEquals(excepted.getDescription(), actual.getDescription());
        assertEquals(excepted.getCoverImage(), actual.getCoverImage());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:/database/book/add-book-war-and-peace.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/database/book/delete-book-war-and-peace.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete book by id")
    public void deleteBook_DeleteBookById_ReturnNoContent() throws Exception {
        mockMvc.perform(delete("/books/4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Search book by parameters")
    public void searchBooks_SearchBooksWithParams_Correct() throws Exception {
        BookDto excepted = new BookDto();
        excepted.setId(1L);
        excepted.setTitle("Harry Potter");
        excepted.setAuthor("Joanne Rowling");
        excepted.setIsbn("12345");
        excepted.setPrice(BigDecimal.valueOf(50));
        excepted.setDescription("Book about boy...");
        excepted.setCoverImage("img.harry.potter");

        MvcResult result = mockMvc.perform(get(
                "/books/search?title=Harry Potter&author=Joanne Rowling")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] searchedBooks = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto[].class);
        BookDto actual = searchedBooks[0];

        assertNotNull(actual);
        assertEquals(excepted, actual);
    }

}
