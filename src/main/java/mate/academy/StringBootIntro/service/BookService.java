package mate.academy.StringBootIntro.service;

import java.util.List;
import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.BookSearchParametersDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto save(CreateBookRequestDto bookDto);

    BookDto updateBookById(Long id, CreateBookRequestDto createBookRequestDto);

    void deleteBookById(Long id);
    List<BookDto> search(BookSearchParametersDto searchParameters);
}
