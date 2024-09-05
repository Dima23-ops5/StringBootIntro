package mate.academy.springbootintro.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.springbootintro.dto.cartitem.CartItemDto;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCartDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    @Autowired
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/shoppingcart/add-information-for-shoppingcart-tests.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/shoppingcart/remove-all-information-for-tests.sql"));
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get shopping cart for user")
   // @Sql(scripts = "classpath:/database/shoppingcart/add-shopping-cart-items.sql",
   // executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
   // @Sql(scripts = "classpath:/database/shoppingcart/remove-all-from-cart-items.sql")
    public void getShoppingCartByCurrentUser_Ok() throws Exception {
        Long userId = 1L;
        ShoppingCartDto excepted = new ShoppingCartDto(1L, userId,
                Set.of(new CartItemDto(1L, 3L, "Harry Potter", 10),
                        new CartItemDto(2L, 2L, "The Hobbit", 15)));


        MvcResult result = mockMvc.perform(get("/cart")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ShoppingCartDto.class);

        assertEquals(excepted.id(), actual.id());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Adding cart item to shopping cart")
    @Sql(scripts = "classpath:/database/shoppingcart/delete-added-cart-item.sql",
    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addCartItemToShoppingCart_Ok() throws Exception {
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(2L, 10);
        ShoppingCartDto excepted = new ShoppingCartDto(1L, 1L,
                Set.of(new CartItemDto(1L,1L, "Harry Potter", 5),
                        new CartItemDto(2L, requestDto.bookId(), "The Hobbit", requestDto.quantity())));
        String requestJson = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/cart")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertNotNull(actual);
        assertEquals(excepted, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Update quantity in cart item")
    @Sql(scripts = "classpath:/database/shoppingcart/add-shopping-cart-items.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:/database/shoppingcart/delete-added-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateQuantityInCartItem() throws Exception {
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(17);
        ShoppingCartDto excepted = new ShoppingCartDto(1L, 1L,
                Set.of(new CartItemDto(1L,1L, "Harry Potter", 5),
                        new CartItemDto(2L, 2L, "The Hobbit", requestDto.quantity())));

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/items/2")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), ShoppingCartDto.class);

        assertNotNull(actual);
        assertEquals(excepted, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Delete cart items by id")
    @Sql(scripts = "classpath:/database/shoppingcart/add-shopping-cart-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteCartItemsById_Ok() throws Exception {
        mockMvc.perform(delete("/items/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
