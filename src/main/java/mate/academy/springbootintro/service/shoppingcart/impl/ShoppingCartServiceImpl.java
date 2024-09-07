package mate.academy.springbootintro.service.shoppingcart.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
import mate.academy.springbootintro.service.cartitem.CartItemService;
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
    private final CartItemService cartItemService;
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
    public CartItemDto addCartItem(
            CreateCartItemRequestDto cartItemRequestDto,
            Long currentUserId
    ) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find user with id: " + currentUserId));
        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(currentUserId);

        if (userShoppingCart == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            userShoppingCart = shoppingCartRepository.save(shoppingCart);
        }
        return cartItemService.save(cartItemRequestDto, userShoppingCart);
    }

    @Override
    @Transactional
    public CartItemDto updateQuantityInCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto) {
        return cartItemService.updateCartItem(cartItemId, updateCartItemRequestDto);
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
