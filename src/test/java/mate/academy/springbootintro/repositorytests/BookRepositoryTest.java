package mate.academy.springbootintro.repositorytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.repository.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = "classpath:database/book/create-book-with-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-book-with-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookByCategoryId_GetBookByExistingCategory_Correct() {
        List<Book> books = bookRepository.getBooksByCategoryId(1L);

        assertNotNull(books);
        assertEquals(1, books.size());
    }

}
