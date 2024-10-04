package mate.academy.springbootintro.service.user;

import mate.academy.springbootintro.dto.userdto.UserRegistrationRequestDto;
import mate.academy.springbootintro.dto.userdto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request);
}
