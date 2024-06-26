package mate.academy.StringBootIntro.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.StringBootIntro.dto.BookSearchParametersDto;
import mate.academy.StringBootIntro.model.Book;
import mate.academy.StringBootIntro.repository.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private BookSpecificationProviderManager bookSpecificationProviderManager;
    @Override
    public Specification<Book> built(BookSearchParametersDto phoneSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (phoneSearchParameters.authors() != null && phoneSearchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(phoneSearchParameters.authors()));
        }
        if (phoneSearchParameters.titles() != null && phoneSearchParameters.titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(phoneSearchParameters.titles()));
        }
        if (phoneSearchParameters.isbns() != null && phoneSearchParameters.isbns().length > 0) {
            specification = specification.and(bookSpecificationProviderManager.getSpecificationProvider("isbn")
                    .getSpecification(phoneSearchParameters.isbns()));
        }
        return specification;
    }
}
