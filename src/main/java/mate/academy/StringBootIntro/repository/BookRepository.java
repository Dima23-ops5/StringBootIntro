package mate.academy.StringBootIntro.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.StringBootIntro.model.Book;

public interface BookRepository {
    public List<Book> findAll();
    public Optional<Book> getBookById(Long id);
    public Book save(Book book);
}
