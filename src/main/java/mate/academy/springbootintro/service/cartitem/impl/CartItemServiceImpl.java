package mate.academy.springbootintro.service.cartitem.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.cartitem.CartItemDto;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.mapper.CartItemMapper;
import mate.academy.springbootintro.model.Book;
import mate.academy.springbootintro.model.CartItem;
import mate.academy.springbootintro.model.ShoppingCart;
import mate.academy.springbootintro.repository.book.BookRepository;
import mate.academy.springbootintro.repository.cartitem.CartItemRepository;
import mate.academy.springbootintro.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.springbootintro.service.cartitem.CartItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartItemDto save(CreateCartItemRequestDto requestDto, ShoppingCart shoppingCart) {
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book with id: "
                        + requestDto.bookId()));

        CartItem cartItem = cartItemMapper.toCartItem(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public CartItemDto updateCartItem(Long id, UpdateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find cart item with id:" + id));
        cartItem.setQuantity(requestDto.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Long id) {

    }
}
