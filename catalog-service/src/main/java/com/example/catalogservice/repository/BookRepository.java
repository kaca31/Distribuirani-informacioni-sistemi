package com.example.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.catalogservice.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
