package mate.academy.springbootintro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static PageRequest pageRequest;
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

        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findAll(pageable)).thenReturn(page);

        List<BookDto> actual = bookService.findAll(pageable);
        List<BookDto> expected = List.of(bookDto);

        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Get book by id")
    public void getBook_GetByIdOneBook_Correct() {
        Book book = createTestBook();
        BookDto bookDto = createBookDto();

        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

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

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.save(book)).thenReturn(book);

        BookDto actual = bookService.save(requestDto);
        assertNotNull(actual);
        assertEquals(actual, bookDto);
    }

    @Test
    @DisplayName("Get book with id that doesn't exist")
    public void getBook_GetBookByNotValidId_NotOk() {
        Book book = createTestBook();
        Long bookId = 100L;
        when(bookRepository.findById(bookId)).thenThrow(
                new NoSuchElementException("Cannot find book with id: " + bookId));

        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.getBookById(bookId));

        String expected = "Cannot find book with id: " + bookId;
        String actual = exception.getMessage();
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Update book")
    public void updateBook_UpdateBookWithExistedId_Correct() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Author");
        requestDto.setTitle("Title");
        requestDto.setIsbn("932523");
        requestDto.setPrice(BigDecimal.valueOf(30));
        requestDto.setCoverImage("img");
        requestDto.setCategoryIds(List.of(2L,3L));

        Book bookUpdated = new Book();
        bookUpdated.setId(1L);
        bookUpdated.setAuthor(requestDto.getAuthor());
        bookUpdated.setTitle(requestDto.getTitle());
        bookUpdated.setPrice(requestDto.getPrice());
        bookUpdated.setCoverImage(requestDto.getCoverImage());
        bookUpdated.setIsbn(requestDto.getIsbn());

        BookDto expected = new BookDto();
        expected.setId(bookUpdated.getId());
        expected.setAuthor(bookUpdated.getAuthor());
        expected.setTitle(bookUpdated.getTitle());
        expected.setDescription(bookUpdated.getDescription());
        expected.setPrice(bookUpdated.getPrice());
        expected.setIsbn(bookUpdated.getIsbn());
        expected.setCoverImage(bookUpdated.getCoverImage());
        expected.setCategoryIds(requestDto.getCategoryIds());
        Book book = createTestBook();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(any(Book.class))).thenReturn(expected);
        when(bookRepository.save(book)).thenReturn(bookUpdated);

        BookDto actual = bookService.updateBookById(1L, requestDto);
        assertNotNull(actual);
        assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Searching book correct")
    public void searchBook_SearchBookBySearchingParam_Correct() {
        Book book = createTestBook();
        BookDto bookDto = createBookDto();
        Page<Book> page = new PageImpl<>(List.of(book));

        BookSearchParametersDto parametersDto =
                new BookSearchParametersDto(null, null,
                        null, null);
        Specification<Book> specification = (root, query, criteriaBuilder) -> null;

        when(bookSpecificationBuilder.built(parametersDto)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageRequest)).thenReturn(page);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.search(parametersDto, pageRequest);
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
