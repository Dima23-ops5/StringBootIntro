package mate.academy.springbootintro.service.impl;

import lombok.AllArgsConstructor;
import mate.academy.springbootintro.dto.userdto.UserRegistrationRequestDto;
import mate.academy.springbootintro.dto.userdto.UserResponseDto;
import mate.academy.springbootintro.mapper.UserMapper;
import mate.academy.springbootintro.repository.user.UserRepository;
import mate.academy.springbootintro.service.UserService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        return userMapper.toDto(
                userRepository.save(userMapper.toModel(request))
        );
    }
}
