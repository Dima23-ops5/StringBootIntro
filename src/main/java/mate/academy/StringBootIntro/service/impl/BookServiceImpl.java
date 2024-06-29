package mate.academy.StringBootIntro.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;
import mate.academy.StringBootIntro.mapper.BookMapper;
import mate.academy.StringBootIntro.model.Book;
import mate.academy.StringBootIntro.repository.BookRepository;
import mate.academy.StringBootIntro.service.BookService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public BookDto save(CreateBookRequestDto bookRequetDto) {
        return bookMapper.toDto(bookRepository
                .save(bookMapper.toModel(bookRequetDto)));
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
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cannot find book with id: "
                        + id));
        book.setIsDeleted(true);
    }

}
