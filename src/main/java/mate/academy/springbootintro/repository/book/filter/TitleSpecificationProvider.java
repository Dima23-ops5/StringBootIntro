package mate.academy.springbootintro.repository.book.filter;

import java.util.Arrays;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
            root.get(TITLE).in(Arrays.stream(params).toArray());
    }
}
