package mate.academy.StringBootIntro.service;

import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;
import mate.academy.StringBootIntro.model.Book;

import java.util.List;

public interface BookService {
    public List<BookDto> findAll();
    public BookDto getBookById(Long id);
    public BookDto save(CreateBookRequestDto bookDto);
}
