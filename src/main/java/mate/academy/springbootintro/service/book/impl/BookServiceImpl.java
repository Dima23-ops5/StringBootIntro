package mate.academy.springbootintro.service.book.impl;

import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import mate.academy.springbootintro.dto.bookdto.BookDto;
import mate.academy.springbootintro.dto.bookdto.BookDtoWithoutCategoryIds;
import mate.academy.springbootintro.dto.bookdto.BookSearchParametersDto;
import mate.academy.springbootintro.dto.bookdto.CreateBookRequestDto;
import mate.academy.springbootintro.mapper.BookMapper;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.repository.book.BookRepository;
import mate.academy.springbootintro.repository.book.filter.BookSpecificationBuilder;
import mate.academy.springbootintro.service.book.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Cannot find book with id: " + id)));
    }

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        return bookMapper.toDto(bookRepository
                .save(bookMapper.toEntity(bookRequestDto)));
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto createBookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Cannot find book with id: " + id));
        bookMapper.updateBookFromDto(createBookRequestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> built = bookSpecificationBuilder.built(searchParameters);
        return bookRepository.findAll(built, pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookRepository.getBooksByCategoryId(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
