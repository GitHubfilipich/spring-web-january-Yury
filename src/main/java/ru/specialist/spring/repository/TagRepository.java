package ru.specialist.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.specialist.spring.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
