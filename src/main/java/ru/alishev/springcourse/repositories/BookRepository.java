package ru.alishev.springcourse.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
  Optional<Book> findByTitleStartingWith(String searchTitle);
}
