package mate.academy.springbootintro.service.shoppingcart.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import mate.academy.springbootintro.service.shoppingcart.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartDto create(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto findShoppingCardByUserId(Long id) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(id));
    }

    @Override
    @Transactional
    public ShoppingCartDto addCartItem(
            CreateCartItemRequestDto requestDto,
            Long currentUserId
    ) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find user with id: " + currentUserId));
        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(currentUserId);

        if (userShoppingCart == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            userShoppingCart = shoppingCart;
        }
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book with id: "
                        + requestDto.bookId()));

        CartItem cartItem = cartItemMapper.toCartItem(requestDto);
        cartItem.setBook(book);
        if (userShoppingCart.getCartItems().stream()
                .filter(cart -> cart.getBook().equals(bookRepository.findById(requestDto.bookId())))
                .findFirst()
                .isPresent()) {
            int newQuantity = (cartItemRepository.findByIdAndShoppingCartId(
                    userShoppingCart.getId(),
                    cartItem.getId()).orElseThrow(() ->
                    new EntityNotFoundException("Cannot find cart item with id:"
                            + cartItem.getId())).getQuantity()) + requestDto.quantity();
            userShoppingCart.getCartItems().stream()
                    .filter(cart -> cart.getBook().equals(
                            bookRepository.findById(requestDto.bookId())))
                    .findFirst()
                    .ifPresent(cart -> cart.setQuantity(newQuantity));
            cartItemRepository.updateQuantityByShoppingCartIdAndItemId(userShoppingCart.getId(),
                    cartItem.getId(),
                    newQuantity);
        } else {
            List<CartItem> cartItems = new ArrayList<>(userShoppingCart.getCartItems());
            cartItems.add(cartItem);
            cartItem.setShoppingCart(userShoppingCart);
            cartItemRepository.save(cartItem);
        }
        return shoppingCartMapper.toDto(shoppingCartRepository.save(userShoppingCart));
    }

    @Override
    @Transactional
    public ShoppingCartDto updateQuantityInCartItem(Long currentUserId,
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto) {
        User user = userRepository.findById(currentUserId).orElseThrow(() ->
                new EntityNotFoundException("Cannot find user with id:" + currentUserId));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        shoppingCart.getCartItems().stream()
                .filter(cart -> cart.getId().equals(cartItemId))
                .findFirst()
                .ifPresent(cartItem -> cartItem.setQuantity(updateCartItemRequestDto.quantity()));
        cartItemRepository.updateQuantityByShoppingCartIdAndItemId(shoppingCart.getId(), cartItemId,
                updateCartItemRequestDto.quantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteCartItem(Long cartItemId, Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find cart item with id: " + cartItemId));

        cartItemRepository.delete(cartItem);
    }

    private void addCartItemToCard(CreateCartItemRequestDto cartItemRequestDto,
                                   Book book,
                                   ShoppingCart shoppingCart) {
        CartItem cartItem = cartItemMapper.toCartItem(cartItemRequestDto);
        cartItem.setBook(book);
        shoppingCart.addCartItemToCart(cartItem);
    }
}
