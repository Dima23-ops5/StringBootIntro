package mate.academy.StringBootIntro.service;

import java.util.List;
import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;

public interface BookService {
    public List<BookDto> findAll();

    public BookDto getBookById(Long id);

    public BookDto save(CreateBookRequestDto bookDto);
}
