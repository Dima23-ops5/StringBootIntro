package mate.academy.springbootintro.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.springbootintro.config.MapperConfig;
import mate.academy.springbootintro.dto.cartitem.CartItemDto;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItem toCartItem(CreateCartItemRequestDto cartItemRequestDto);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Named(value = "cartItemToCartItemDtos")
    default Set<CartItemDto> cartItemToCartItemDtos(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }
}
