package mate.academy.StringBootIntro.service;

import mate.academy.StringBootIntro.model.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);

    List findAll();
}
