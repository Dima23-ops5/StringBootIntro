package mate.academy.springbootintro.mapper;

import mate.academy.springbootintro.config.MapperConfig;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCartDto;
import mate.academy.springbootintro.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItemSet",
            qualifiedByName = "cartItemToCartItemDtos")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
