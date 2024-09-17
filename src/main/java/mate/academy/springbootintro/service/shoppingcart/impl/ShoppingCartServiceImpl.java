package mate.academy.springbootintro.service.shoppingcart.impl;

import jakarta.persistence.EntityNotFoundException;
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
            Long userId
    ) {
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Book with id: %d is not found", requestDto.bookId())
                ));
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(requestDto.bookId()))
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity()
                                + requestDto.quantity()),
                        () -> addCartItemToCart(requestDto, book, cart));
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateQuantityInCartItem(Long userId,
            Long cartItemId,
            UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .map(item -> {
                    item.setQuantity(requestDto.quantity());
                    return item;
                }).orElseThrow(() -> new EntityNotFoundException(
                        String.format("No cart item with id: %d for user: %d", cartItemId, userId)
                ));
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
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

    private void addCartItemToCart(CreateCartItemRequestDto requestDto,
                                   Book book, ShoppingCart cart) {
        CartItem cartItem = cartItemMapper.toCartItem(requestDto);
        cartItem.setBook(book);
        cart.addItemToCart(cartItem);
    }
}
