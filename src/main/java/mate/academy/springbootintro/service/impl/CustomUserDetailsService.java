package mate.academy.springbootintro.service.impl;

import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import mate.academy.springbootintro.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                "Cannot find user with email:" + email
                        )
                );
    }
}
