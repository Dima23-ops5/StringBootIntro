package mate.academy.springbootintro.repository.categoty;

import mate.academy.springbootintro.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
