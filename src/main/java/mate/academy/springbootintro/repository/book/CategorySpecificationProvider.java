package mate.academy.springbootintro.repository.book;

import java.util.Arrays;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecificationProvider implements SpecificationProvider<Book> {
    private static final String CATEGORY = "category";

    @Override
    public String getKey() {
        return CATEGORY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get(CATEGORY).in(Arrays.stream(params).toArray());
    }
}
