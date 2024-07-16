package mate.academy.springbootintro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.springbootintro.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.springbootintro.dto.shoppingcartdto.ShoppingCardDto;
import mate.academy.springbootintro.model.User;
import mate.academy.springbootintro.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize(value = "hasRole('USER')")
    @Operation(
            summary = "Get shopping cart",
            description = "Getting shopping cart for current user"
    )
    public ShoppingCardDto getShoppingCard() {
        return shoppingCartService.findShoppingCardByUserId(getCurrentUserId());
    }

    @PostMapping
    @PreAuthorize(value = "hasRole('USER')")
    @Operation(
            summary = "Add item carts to shopping cart",
            description = "Adding item carts to users shopping cart"
    )
    public ShoppingCardDto addItemToCart(
            @RequestBody @Valid CreateCartItemRequestDto cartItemRequestDto
    ) {
        return shoppingCartService.addCartItem(cartItemRequestDto, getCurrentUserId());
    }

    @PutMapping(value = "/items/{cartItemId}")
    @PreAuthorize(value = "hasRole('USER')")
    @Operation(
            summary = "Update quantity",
            description = "Updating products quantity in item carts"
    )
    public ShoppingCardDto updateQuantity(
            @PathVariable @Positive Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto updateCartItemRequestDto
    ) {
        return shoppingCartService.updateQuantityInCartItem(
                cartItemId,
                updateCartItemRequestDto,
                getCurrentUserId()
        );
    }

    @DeleteMapping(value = "/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(value = "hasRole('USER')")
    @Operation(summary = "Delete item cart", description = "Deleting item carts in shopping cart")
    public void deleteItemCart(Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
