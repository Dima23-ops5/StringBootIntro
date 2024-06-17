package mate.academy.StringBootIntro.repository;

import mate.academy.StringBootIntro.dto.BookDto;
import mate.academy.StringBootIntro.dto.CreateBookRequestDto;
import mate.academy.StringBootIntro.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    public List<Book> findAll();
    public Optional<Book> getBookById(Long id);
    public Book save(Book book);
}
