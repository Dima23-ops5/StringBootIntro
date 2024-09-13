package mate.academy.springbootintro.repository.user;

import java.util.Optional;
import mate.academy.springbootintro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<UserDetails> findByEmail(String email);

    Optional<User> findById(Long id);

    boolean existsByEmail(String email);
}
