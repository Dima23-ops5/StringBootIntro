package mate.academy.springbootintro.repository;

import mate.academy.springbootintro.dto.bookdto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> built(BookSearchParametersDto searchParameters);
}
