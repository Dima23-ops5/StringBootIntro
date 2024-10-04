package mate.academy.springbootintro.mapper;

import mate.academy.springbootintro.config.MapperConfig;
import mate.academy.springbootintro.dto.userdto.UserRegistrationRequestDto;
import mate.academy.springbootintro.dto.userdto.UserResponseDto;
import mate.academy.springbootintro.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRequestDto);
}
