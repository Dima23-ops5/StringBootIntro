package mate.academy.springbootintro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    @Sql(scripts = "classpath:database/category/create-book-with-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/delete-book-with-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookByCategoryId_GetBookByExistingCategory_Correct() {
        List<Book> books = bookRepository.getBooksByCategoryId(4L);

        assertNotNull(books);
        assertEquals(1, books.size());
    }

}
