package mate.academy.StringBootIntro.repository;

import mate.academy.StringBootIntro.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
