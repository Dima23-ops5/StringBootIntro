package mate.academy.springbootintro.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.userdto.UserRegistrationRequestDto;
import mate.academy.springbootintro.dto.userdto.UserResponseDto;
import mate.academy.springbootintro.exeption.RegistrationException;
import mate.academy.springbootintro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication manager", description = "Endpoints for authentication users")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register users", description = "Register new users")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto request
    ) throws RegistrationException {
        return userService.register(request);
    }
}
