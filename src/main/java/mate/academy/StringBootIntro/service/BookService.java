package mate.academy.StringBootIntro.service;

import java.util.List;
import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;

public interface BookService {
    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto save(CreateBookRequestDto bookDto);

    BookDto updateBookById(Long id, CreateBookRequestDto createBookRequestDto);

    void deleteBookById(Long id);
}
