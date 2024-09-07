package mate.academy.springbootintro.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.springbootintro.dto.cartitem.CartItemDto;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCartDto;
import mate.academy.springbootintro.service.shoppingcart.ShoppingCartService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    @Autowired
    protected static MockMvc mockMvc;

    @Mock
    private ShoppingCartService shoppingCartService;
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
                    new ClassPathResource(
                            "database/shoppingcart/add-information-for-shoppingcart-tests.sql"));
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/shoppingcart/remove-all-information-for-tests.sql"));
        }
    }

    @Test
    @DisplayName("Get shopping cart for user")
    @WithUserDetails("alice@gmail.com")
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

    @Test
    @DisplayName("Adding cart item to shopping cart")
    @Sql(scripts = "classpath:/database/shoppingcart/delete-added-cart-item.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("alice@gmail.com")
    public void addCartItemToShoppingCart_Ok() throws Exception {
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(3L, 10);
        CartItemDto excepted = new CartItemDto(3L, requestDto.bookId(), "The Hobbit",
                requestDto.quantity());
        String requestJson = objectMapper.writeValueAsString(requestDto);

        when(shoppingCartService.addCartItem(requestDto, 1L)).thenReturn(excepted);

        MvcResult result = mockMvc.perform(post("/cart")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CartItemDto.class);

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
    @WithUserDetails("alice@gmail.com")
    public void updateQuantityInCartItem() throws Exception {
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(17);
        CartItemDto excepted = new CartItemDto(2L, 3L, "The Hobbit", requestDto.quantity());

        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/cart/items/2")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CartItemDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CartItemDto.class);

        assertNotNull(actual);
        assertEquals(excepted, actual);
    }

    @Test
    @DisplayName("Delete cart items by id")
    @Sql(scripts = "classpath:/database/shoppingcart/add-shopping-cart-items.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithUserDetails("alice@gmail.com")
    public void deleteCartItemsById_Ok() throws Exception {
        mockMvc.perform(delete("/cart/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
