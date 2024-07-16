package mate.academy.springbootintro.service.shoppingcart.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCardDto;
import mate.academy.springbootintro.mapper.ShoppingCardMapper;
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
    private final ShoppingCardMapper shoppingCardMapper;

    @Override
    @Transactional
    public ShoppingCardDto findShoppingCardByUserId(Long id) {
        return shoppingCardMapper.toDto(
                shoppingCartRepository.findByUserId(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Cannot find shopping cart by user with id: "
                                                + id
                                )
                        )
        );
    }

    @Override
    @Transactional
    public ShoppingCardDto addCartItem(
            CreateCartItemRequestDto cartItemRequestDto,
            Long currentUserId
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(
                currentUserId
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot found shopping card with user id: "
                                + currentUserId
                )
        );
        Book book = bookRepository.findById(
                cartItemRequestDto.bookId()
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot found book with id: "
                                + cartItemRequestDto.bookId()
                )
        );
        CartItem cartItem = cartItemRepository.findByShoppingCartAndBook(
                shoppingCart, book
        ).orElse(null);
        if (cartItem == null) {
            CartItem newCartItem = new CartItem();
            newCartItem.setShoppingCart(shoppingCart);
            newCartItem.setBook(book);
            newCartItem.setQuantity(cartItemRequestDto.quantity());
            cartItemRepository.save(newCartItem);
            shoppingCart.getCartItems().add(newCartItem);
        } else {
            shoppingCart.getCartItems().remove(cartItem);
            cartItem.setQuantity(
                    cartItem.getQuantity()
                    + cartItemRequestDto.quantity()
            );
            cartItemRepository.save(cartItem);
            shoppingCart.getCartItems().add(cartItem);
        }
        return shoppingCardMapper.toDto(
                shoppingCartRepository.save(shoppingCart)
        );
    }

    @Override
    @Transactional
    public ShoppingCardDto updateQuantityInCartItem(
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto,
            Long currentUserId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(
                currentUserId
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot found shopping card with user id: "
                                + currentUserId
                )
        );
        CartItem cartItem = cartItemRepository.findById(
                cartItemId
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot found cart item with id: "
                                + cartItemId
                )
        );
        shoppingCart.getCartItems().remove(cartItem);
        cartItem.setQuantity(updateCartItemRequestDto.quantity());
        cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);

        return shoppingCardMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(
                cartItemId
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        "Cannot find cart item with id: "
                                + cartItemId
                )
        );

        cartItemRepository.delete(cartItem);
    }
}
