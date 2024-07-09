package mate.academy.springbootintro.repository.book;

import java.util.Arrays;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN = "isbn";

    @Override
    public String getKey() {
        return ISBN;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("isbn").in(Arrays.stream(params).toArray());
    }
}
