package mate.academy.springbootintro.service.book;

import java.util.List;
import mate.academy.springbootintro.dto.bookdto.BookDto;
import mate.academy.springbootintro.dto.bookdto.BookDtoWithoutCategoryIds;
import mate.academy.springbootintro.dto.bookdto.BookSearchParametersDto;
import mate.academy.springbootintro.dto.bookdto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto save(CreateBookRequestDto bookDto);

    BookDto updateBookById(Long id, CreateBookRequestDto createBookRequestDto);

    void deleteBookById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);
}
