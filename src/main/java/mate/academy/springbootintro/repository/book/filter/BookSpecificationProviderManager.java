package mate.academy.springbootintro.repository.book.filter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.repository.SpecificationProvider;
import mate.academy.springbootintro.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        SpecificationProvider<Book> bookSpecificationProvider = specificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Cannot find correct specification for key: " + key));
        return bookSpecificationProvider;
    }
}
