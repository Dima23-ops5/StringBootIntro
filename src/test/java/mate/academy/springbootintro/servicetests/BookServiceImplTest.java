package mate.academy.springbootintro.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import mate.academy.springbootintro.dto.bookdto.BookDto;
import mate.academy.springbootintro.dto.bookdto.BookSearchParametersDto;
import mate.academy.springbootintro.dto.bookdto.CreateBookRequestDto;
import mate.academy.springbootintro.mapper.BookMapper;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.repository.book.BookRepository;
import mate.academy.springbootintro.repository.book.filter.BookSpecificationBuilder;
import mate.academy.springbootintro.service.book.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Find all books")
    public void findAll_ThreeBooks_Correct() {

        Book book = createTestBook();

        BookDto bookDto = createBookDto();

        Page<Book> page = new PageImpl<>(List.of(book));
        Pageable pageable = PageRequest.of(0, 10);

        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);
        Mockito.when(bookRepository.findAll(pageable)).thenReturn(page);

        List<BookDto> actual = bookService.findAll(pageable);
        List<BookDto> expected = List.of(bookDto);

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Get book by id")
    public void getBook_GetByIdOneBook_Correct() {
        Book book = createTestBook();
        BookDto bookDto = createBookDto();

        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto actual = bookService.getBookById(1L);

        assertNotNull(actual);
        assertEquals(actual, bookDto);
    }

    @Test
    @DisplayName("Save book")
    public void saveBook_saveOneBook_Correct() {
        BookDto bookDto = createBookDto();

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor(bookDto.getAuthor());
        requestDto.setIsbn(bookDto.getIsbn());
        requestDto.setPrice(bookDto.getPrice());
        requestDto.setTitle(bookDto.getTitle());
        requestDto.setDescription(bookDto.getDescription());
        requestDto.setCoverImage(bookDto.getCoverImage());
        Book book = createTestBook();

        Mockito.when(bookMapper.toEntity(requestDto)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        BookDto actual = bookService.save(requestDto);
        assertNotNull(actual);
        assertEquals(actual, bookDto);
    }

    @Test
    @DisplayName("Get book with id that doesn't exist")
    public void getBook_GetBookById_False() {
        Book book = createTestBook();
        Long bookId = 100L;
        Mockito.when(bookRepository.findById(bookId)).thenThrow(
                new NoSuchElementException("Cannot find book with id: " + bookId));

        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.getBookById(bookId));

        String expected = "Cannot find book with id: " + bookId;
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Update book by Id")
    public void updateBook_UpdateBookById_Correct() {
        Book book = createTestBook();
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Updated author");
        createBookRequestDto.setDescription("Updated description");
        createBookRequestDto.setPrice(BigDecimal.valueOf(35));
        createBookRequestDto.setTitle("Updated title");
        createBookRequestDto.setIsbn("Updated isbn");
        createBookRequestDto.setCoverImage("Updated cover image");

        BookDto bookDto = createBookDto();
        bookDto.setAuthor(createBookRequestDto.getAuthor());
        bookDto.setTitle(createBookRequestDto.getTitle());
        bookDto.setPrice(createBookRequestDto.getPrice());
        bookDto.setDescription(createBookRequestDto.getDescription());
        bookDto.setIsbn(createBookRequestDto.getIsbn());
        bookDto.setCoverImage(createBookRequestDto.getCoverImage());

        Mockito.when(bookService.updateBookById(1L,
                createBookRequestDto)).thenReturn(bookDto);

        BookDto expected = bookDto;
        BookDto actual = bookService.updateBookById(1L, createBookRequestDto);

        assertEquals(expected, actual);
    }

    @Test
    public void searchBook_SearchBookBySearchingParam_Correct() {
        Book book = createTestBook();
        BookDto bookDto = createBookDto();

        BookSearchParametersDto parametersDto =
                new BookSearchParametersDto(null, null,
                        null, null);
        Specification<Book> specification = (root, query, criteriaBuilder) -> null;

        Mockito.when(bookSpecificationBuilder.built(parametersDto)).thenReturn(specification);
        Mockito.when(bookRepository.findAll(specification)).thenReturn(List.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.search(parametersDto);
        assertEquals(1, result.size());
        assertEquals(bookDto, result.get(0));
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Harry Potter");
        book.setAuthor("Joanne Rowling");
        book.setPrice(BigDecimal.valueOf(50));
        book.setDescription("Book about boy...");
        book.setIsbn("13432");
        book.setCoverImage("img");
        return book;
    }

    private BookDto createBookDto() {
        Book book = createTestBook();
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCoverImage(book.getCoverImage());

        return bookDto;
    }
}
