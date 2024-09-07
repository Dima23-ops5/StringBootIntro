package mate.academy.springbootintro.repository;

import static org.junit.Assert.assertNotNull;

import mate.academy.springbootintro.model.ShoppingCart;
import mate.academy.springbootintro.model.User;
import mate.academy.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @Sql(scripts = "classpath:/database/shoppingcart/add-shoppingcart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/database/shoppingcart/delete-shoppingcart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getShoppingCartByUserId_Ok() {
        User user = new User();
        user.setId(1L);
        user.setEmail("useremail@gmail.com");
        user.setPassword("userpassword");
        user.setFirstName("user");
        user.setLastName("userLastname");
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());

        assertNotNull(shoppingCart);
    }
}
