package mate.academy.springbootintro.service;

import mate.academy.springbootintro.dto.userdto.UserRegistrationRequestDto;
import mate.academy.springbootintro.dto.userdto.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request);
}
