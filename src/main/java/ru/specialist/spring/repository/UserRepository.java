package ru.specialist.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.specialist.spring.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
