package mate.academy.springbootintro.security;

import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.dto.userdto.UserLoginRequestDto;
import mate.academy.springbootintro.dto.userdto.UserLoginResponseDto;
import mate.academy.springbootintro.exeption.LoginException;
import mate.academy.springbootintro.model.User;
import mate.academy.springbootintro.repository.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(requestDto.email(),
                                requestDto.password())
                );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
