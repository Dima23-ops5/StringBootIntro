package mate.academy.springbootintro.repository.book.filter;

import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.bookdto.BookSearchParametersDto;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.repository.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR = "author";
    private static final String TITLE = "title";
    private static final String ISBN = "isbn";
    private static final String CATEGORY = "category";
    private final BookSpecificationProviderManager bookSpecificationProviderManager;

    @Override
    public Specification<Book> built(BookSearchParametersDto bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
            specification = specification.and(
                    bookSpecificationProviderManager
                            .getSpecificationProvider(AUTHOR)
                            .getSpecification(bookSearchParameters.authors())
            );
        }
        if (bookSearchParameters.titles() != null
                && bookSearchParameters.titles().length > 0
        ) {
            specification = specification.and(
                    bookSpecificationProviderManager
                            .getSpecificationProvider(TITLE)
                            .getSpecification(bookSearchParameters.titles())
            );
        }
        if (bookSearchParameters.isbn() != null && bookSearchParameters.isbn().length > 0) {
            specification = specification.and(
                    bookSpecificationProviderManager
                            .getSpecificationProvider(ISBN)
                            .getSpecification(bookSearchParameters.isbn())
            );
        }
        if (bookSearchParameters.categories() != null
                && bookSearchParameters.categories().length > 0) {
            specification = specification.and(
              bookSpecificationProviderManager.getSpecificationProvider(CATEGORY)
                      .getSpecification(bookSearchParameters.categories())
            );
        }
        return specification;
    }
}
