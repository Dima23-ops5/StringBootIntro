package mate.academy.StringBootIntro.repository;

import mate.academy.StringBootIntro.dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> built(BookSearchParametersDto phoneSearchParameters);
}
