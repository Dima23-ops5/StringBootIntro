package mate.academy.StringBootIntro.service.impl;

import mate.academy.StringBootIntro.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import mate.academy.StringBootIntro.repository.BookRepository;
import mate.academy.StringBootIntro.service.BookService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List findAll() {
        return bookRepository.findAll();
    }
}
