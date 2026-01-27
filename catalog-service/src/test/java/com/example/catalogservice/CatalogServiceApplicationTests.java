package com.example.catalogservice;

import org.junit.jupiter.api.Test;

import com.example.catalogservice.dto.BookDto;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.repository.BookRepository;
import com.example.catalogservice.service.BookService;

import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CatalogServiceApplicationTests {

	private BookRepository repo;
	private BookService service;

	@BeforeEach
	void setup() {
		repo = mock(BookRepository.class);
		service = new BookService(repo);
	}

	@Test
	void testCreate_NewBook() {
		BookDto dto = BookDto.builder()
				.title("The Pragmatic Programmer")
				.author("Andrew Hunt & David Thomas")
				.price(BigDecimal.valueOf(42.50))
				.stockQuantity(12)
				.build();

		Book saved = Book.builder()
				.id(100L)
				.title("The Pragmatic Programmer")
				.author("Andrew Hunt & David Thomas")
				.price(BigDecimal.valueOf(42.50))
				.stockQuantity(12)
				.build();

		when(repo.save(any(Book.class))).thenReturn(saved);

		BookDto result = service.create(dto);

		assertNotNull(result);
		assertEquals(100L, result.getId());
		assertEquals("The Pragmatic Programmer", result.getTitle());
		verify(repo, times(1)).save(any(Book.class));
	}

	@Test
	void testGetBook_Found() {
		Book book = Book.builder()
				.id(10L)
				.title("Clean Code")
				.author("Robert C. Martin")
				.price(BigDecimal.valueOf(39.99))
				.stockQuantity(30)
				.build();

		when(repo.findById(10L)).thenReturn(Optional.of(book));

		BookDto dto = service.get(10L);

		assertNotNull(dto);
		assertEquals("Clean Code", dto.getTitle());
		assertEquals("Robert C. Martin", dto.getAuthor());
	}

	@Test
	void testGetBook_NotFound() {
		when(repo.findById(999L)).thenReturn(Optional.empty());

		BookDto dto = service.get(999L);

		assertNull(dto);
	}

	@Test
	void testGetAllBooks() {
		List<Book> books = List.of(
				Book.builder().id(1L).title("Clean Code").author("Robert C. Martin")
						.price(BigDecimal.valueOf(39.99)).stockQuantity(20).build(),
				Book.builder().id(2L).title("Domain-Driven Design").author("Eric Evans")
						.price(BigDecimal.valueOf(55.00)).stockQuantity(7).build(),
				Book.builder().id(3L).title("Effective Java").author("Joshua Bloch")
						.price(BigDecimal.valueOf(45.00)).stockQuantity(13).build());

		when(repo.findAll()).thenReturn(books);

		List<BookDto> result = service.getAll();

		assertEquals(3, result.size());
		assertEquals("Domain-Driven Design", result.get(1).getTitle());
		assertEquals("Joshua Bloch", result.get(2).getAuthor());
	}

	@Test
	void testUpdateBook() {
		Book existing = Book.builder()
				.id(5L)
				.title("Old Title")
				.author("Old Author")
				.price(BigDecimal.valueOf(5.0))
				.stockQuantity(1)
				.build();

		BookDto dto = BookDto.builder()
				.title("Refactoring")
				.author("Martin Fowler")
				.price(BigDecimal.valueOf(60.00))
				.stockQuantity(15)
				.build();

		when(repo.findById(5L)).thenReturn(Optional.of(existing));
		when(repo.save(existing)).thenReturn(existing);

		BookDto updated = service.update(5L, dto);

		assertEquals("Refactoring", updated.getTitle());
		assertEquals("Martin Fowler", updated.getAuthor());
		assertEquals(BigDecimal.valueOf(60.00), updated.getPrice());
		assertEquals(15, updated.getStockQuantity());
	}

	@Test
	void testDeleteBook() {
		service.delete(50L);

		verify(repo, times(1)).deleteById(50L);
	}
}