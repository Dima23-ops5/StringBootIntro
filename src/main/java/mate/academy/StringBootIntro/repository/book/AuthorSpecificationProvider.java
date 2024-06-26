package mate.academy.StringBootIntro.repository.book;

import java.util.Arrays;
import mate.academy.StringBootIntro.model.Book;
import mate.academy.StringBootIntro.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("author").in(Arrays.stream(params).toArray());
    }
}
