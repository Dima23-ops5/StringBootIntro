package mate.academy.springbootintro.service.user.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.AllArgsConstructor;
import mate.academy.springbootintro.dto.userdto.UserRegistrationRequestDto;
import mate.academy.springbootintro.dto.userdto.UserResponseDto;
import mate.academy.springbootintro.exeption.RegistrationException;
import mate.academy.springbootintro.mapper.UserMapper;
import mate.academy.springbootintro.model.Role;
import mate.academy.springbootintro.model.User;
import mate.academy.springbootintro.repository.role.RoleRepository;
import mate.academy.springbootintro.repository.user.UserRepository;
import mate.academy.springbootintro.service.shoppingcart.ShoppingCartService;
import mate.academy.springbootintro.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException(
                    "Can't register new user with email: "
                    + request.getEmail());
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRole(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find role with name: " + Role.RoleName.USER))));
        userRepository.save(user);
        shoppingCartService.create(user);
        return userMapper.toDto(user);
    }
}
