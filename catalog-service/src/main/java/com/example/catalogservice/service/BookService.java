package com.example.catalogservice.service;

import com.example.catalogservice.dto.BookDto;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository repo;
    public BookService(BookRepository repo){ this.repo = repo; }

    public BookDto create(BookDto dto){
        Book b = Book.builder().title(dto.getTitle()).author(dto.getAuthor()).price(dto.getPrice()).stockQuantity(dto.getStockQuantity()).build();
        return toDto(repo.save(b));
    }
    public BookDto get(Long id){ return repo.findById(id).map(this::toDto).orElse(null); }
    public List<BookDto> getAll(){ return repo.findAll().stream().map(this::toDto).collect(Collectors.toList()); }
    public BookDto update(Long id, BookDto dto){ Book b = repo.findById(id).orElseThrow(); b.setTitle(dto.getTitle()); b.setAuthor(dto.getAuthor()); b.setPrice(dto.getPrice()); b.setStockQuantity(dto.getStockQuantity()); return toDto(repo.save(b)); }
    public void delete(Long id){ repo.deleteById(id); }
    private BookDto toDto(Book b){ return BookDto.builder().id(b.getId()).title(b.getTitle()).author(b.getAuthor()).price(b.getPrice()).stockQuantity(b.getStockQuantity()).build(); }
}
