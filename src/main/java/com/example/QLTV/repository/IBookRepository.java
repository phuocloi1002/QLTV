package com.example.QLTV.repository;

import com.example.QLTV.enity.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IBookRepository extends JpaRepository<Book, UUID> {

    // Ép nạp dữ liệu copies để Mapper có thể calculateTotal()
    @EntityGraph(attributePaths = {"copies"})
    List<Book> findAll();

    @EntityGraph(attributePaths = {"copies"})
    Optional<Book> findById(UUID id);

    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
}