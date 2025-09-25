package com.example.catalog_service.controller;

import com.example.catalog_service.Book;
import com.example.catalog_service.dto.BookDTO;
import com.example.catalog_service.repository.BookRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    private BookDTO mapToDTO(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice());
    }

    private Book mapToEntity(BookDTO dto) {
        return new Book(dto.getId(), dto.getTitle(), dto.getAuthor(), dto.getPrice());
    }

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(value -> ResponseEntity.ok(mapToDTO(value)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<BookDTO> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author) {

        List<Book> books;

        if (title != null) {
            books = bookRepository.findByTitleContainingIgnoreCase(title);
        } else if (author != null) {
            books = bookRepository.findByAuthorContainingIgnoreCase(author);
        } else {
            books = bookRepository.findAll();
        }

        return books.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        Book savedBook = bookRepository.save(mapToEntity(bookDTO));
        return ResponseEntity.ok(mapToDTO(savedBook));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookDTO.setId(id);
        Book updatedBook = bookRepository.save(mapToEntity(bookDTO));
        return ResponseEntity.ok(mapToDTO(updatedBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}