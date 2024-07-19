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
import mate.academy.springbootintro.repository.book.BookRepository;
import mate.academy.springbootintro.repository.cartitem.CartItemRepository;
import mate.academy.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.springbootintro.service.shoppingcart.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCardMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public ShoppingCartDto findShoppingCardByUserId(Long id) {
        return shoppingCardMapper.toDto(shoppingCartRepository.findByUserId(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                                        String.format(
                                                "Cannot find shopping cart by user with id: %d", id
                                        )
                                ))
        );
    }

    @Override
    @Transactional
    public ShoppingCartDto addCartItem(
            CreateCartItemRequestDto cartItemRequestDto,
            Long currentUserId
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Cannot found shopping card with user id: %d", currentUserId)
                )
        );
        Book book = bookRepository.findById(cartItemRequestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Cannot found book with id: %d", cartItemRequestDto.bookId())
                )
        );
        shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(cartItemRequestDto.bookId()))
                .findFirst()
                .ifPresentOrElse(cartItem -> cartItem.setQuantity(cartItem.getQuantity()
                        + cartItemRequestDto.quantity()),
                        () -> addCartItemToCard(cartItemRequestDto, book, shoppingCart));
        return shoppingCardMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    @Transactional
    public ShoppingCartDto updateQuantityInCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto,
            Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(
                userId
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot found shopping card with user id: "
                                + userId
                )
        );
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId())
                .map(item -> {
                    item.setQuantity(updateCartItemRequestDto.quantity());
                    return item;
                }).orElseThrow(() -> new EntityNotFoundException(
                        String.format("No cart item with id: %d for user: %d", cartItemId, userId)
                ));
        cartItemRepository.save(cartItem);
        return shoppingCardMapper.toDto(cart);
    }

    @Override
    public void deleteCartItem(Long cartItemId, Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(
                userId
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Cannot found shopping card with user id: %d ", userId
                ))
        );
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format(
                                        "Cannot found item cart with id: %d and user id: %d",
                                        cartItemId, userId
                        ))
                );

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
