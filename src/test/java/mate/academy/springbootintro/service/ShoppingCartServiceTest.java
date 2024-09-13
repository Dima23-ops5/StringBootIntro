package mate.academy.springbootintro.service;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import mate.academy.springbootintro.dto.cartitem.CartItemDto;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCartDto;
import mate.academy.springbootintro.mapper.CartItemMapper;
import mate.academy.springbootintro.mapper.ShoppingCartMapper;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.model.CartItem;
import mate.academy.springbootintro.model.ShoppingCart;
import mate.academy.springbootintro.model.User;
import mate.academy.springbootintro.repository.book.BookRepository;
import mate.academy.springbootintro.repository.cartitem.CartItemRepository;
import mate.academy.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.springbootintro.repository.user.UserRepository;
import mate.academy.springbootintro.service.shoppingcart.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find shopping cart by user id")
    public void findShoppingCartByUserId_Ok() {
        User user = createUser();
        ShoppingCart shoppingCart = createDefaultShoppingCart(user);
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(shoppingCart.getId(),
                shoppingCart.getUser().getId(),Set.of(new CartItemDto(1L, 2L, "Harry Potter", 10)));

        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto actual = shoppingCartService.findShoppingCardByUserId(user.getId());

        assertNotNull(actual);
        assertEquals(shoppingCartDto, actual);
    }

    @Test
    @DisplayName("find shopping cart by not existed id")
    public void findShoppingCartByUserId_ReturnNull() {
        Long userId = 100L;
        String excepted = "Cannot find shopping cart with user which id is: " + userId;
        when(shoppingCartRepository.findByUserId(userId)).thenThrow(
                new NoSuchElementException("Cannot find shopping cart "
                        + "with user which id is: " + userId));
        try {
            ShoppingCartDto shoppingCartDto = shoppingCartService.findShoppingCardByUserId(userId);
            assertNull(shoppingCartDto);
        } catch (NoSuchElementException actual) {
            assertEquals(excepted, actual.getMessage());
        }
    }

    @Test
    @DisplayName("Add cart items to shopping cart entity")
    public void addCartItemsToShoppingCart_Ok() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("bookTitle");
        book.setAuthor("bookAuthor");
        book.setDescription("bookDescription");
        book.setCoverImage("bookCoverImg");
        book.setPrice(BigDecimal.valueOf(100));
        book.setCategories(new HashSet<>());

        User user = createUser();
        ShoppingCart shoppingCart = createDefaultShoppingCart(user);

        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(book.getId(), 10);

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setId(1L);
        cartItem.setQuantity(requestDto.quantity());

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);

        ShoppingCart exceptedCart = new ShoppingCart();
        exceptedCart.setId(shoppingCart.getId());
        exceptedCart.setUser(shoppingCart.getUser());
        exceptedCart.setCartItems(cartItems);

        CartItemDto cartItemDto = new CartItemDto(cartItem.getId(), cartItem.getBook().getId(),
                cartItem.getBook().getTitle(), cartItem.getQuantity());

        Set<CartItemDto> cartItemDtos = new HashSet<>();
        cartItemDtos.add(cartItemDto);

        ShoppingCartDto excepted = new ShoppingCartDto(exceptedCart.getId(),
                exceptedCart.getUser().getId(), cartItemDtos);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(shoppingCart);
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(exceptedCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(excepted);
        when(cartItemMapper.toCartItem(requestDto)).thenReturn(cartItem);

        ShoppingCartDto actual = shoppingCartService.addCartItem(requestDto, user.getId());

        assertNotNull(actual);
        assertEquals(excepted, actual);
    }

    @Test
    @DisplayName("Update quantity in shopping cart")
    public void updateQuantityInShoppingCart_Ok() {
        User user = createUser();
        ShoppingCart shoppingCart = createDefaultShoppingCart(user);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(new Book());
        cartItem.setQuantity(10);
        cartItem.setShoppingCart(shoppingCart);

        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(5);

        CartItemDto cartItemDto = new CartItemDto(cartItem.getId(),
                cartItem.getBook().getId(), cartItem.getBook().getTitle(),
                requestDto.quantity());

        shoppingCart.setCartItems(Set.of(cartItem));

        CartItem exceptedCartItem = new CartItem();
        exceptedCartItem.setId(cartItemDto.id());
        exceptedCartItem.setBook(new Book());
        exceptedCartItem.setQuantity(requestDto.quantity());
        exceptedCartItem.setShoppingCart(shoppingCart);

        ShoppingCartDto excepted = new ShoppingCartDto(shoppingCart.getId(),
                shoppingCart.getUser().getId(), Set.of(cartItemDto));

        when(cartItemRepository.findByIdAndShoppingCartId(anyLong(),
                anyLong())).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(excepted);
        when(shoppingCartRepository.findByUserId(anyLong())).thenReturn(shoppingCart);

        ShoppingCartDto actual = shoppingCartService.updateQuantityInCartItem(user.getId(),
                cartItem.getId(), requestDto);

        assertNotNull(actual);
        assertEquals(excepted, actual);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("useremail@gmail.com");
        user.setPassword("userpassword");
        user.setFirstName("user");
        user.setLastName("userLastName");
        user.setShippingAddress("Kreschistyk street");

        return user;
    }

    private ShoppingCart createDefaultShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(Set.of());

        return shoppingCart;
    }
}
