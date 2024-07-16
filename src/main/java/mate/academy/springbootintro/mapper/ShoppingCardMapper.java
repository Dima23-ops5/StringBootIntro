package mate.academy.springbootintro.mapper;

import mate.academy.springbootintro.config.MapperConfig;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCardDto;
import mate.academy.springbootintro.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCardMapper {
    ShoppingCardDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(ShoppingCardDto shoppingCardDto);
}
